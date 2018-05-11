package eu.ace_design.instances.neo4j

import java.io.File

import eu.ace_design.kernel.modules.query.{Query, QueryEngine}
import eu.ace_design.kernel.modules.storage.{HistoryEvent, StorageAction, StorageActionEngine}
import eu.ace_design.kernel.graph.{GraphStorage, Node, Transactional}
import org.neo4j.graphdb._
import org.neo4j.graphdb.factory.GraphDatabaseFactory


object Neo4J extends GraphStorage[Cypher] with Transactional[Transaction]
  with StorageActionEngine[Cypher] with QueryEngine[Cypher]{

  private val databaseDirectory = new File(System.getProperty("java.io.tmpdir") + "neo4j")

  private val db: GraphDatabaseService = {
    logger.info(s"Creating an embedded Neo4J database located at [$databaseDirectory]")
    val result = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(databaseDirectory).newGraphDatabase()
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        logger.info("Shutting down the Neo4J embedded database")
        result.shutdown()
        logger.info(s"Deleting [$databaseDirectory] contents")
        org.neo4j.io.fs.FileUtils.deleteRecursively(databaseDirectory)
      }
    })
    result
  }

  override protected def begin(): Transaction           = db.beginTx()
  override protected def success(tx: Transaction): Unit = tx.success()
  override protected def fail(tx: Transaction): Unit    = tx.failure()
  override protected def close(tx: Transaction): Unit   = tx.close()

  override protected def _execVoid(action: StorageAction[Cypher]): Map[HistoryEvent.Value, Int] = {
    try {
      val query = action.translate.toString
      logger.info(s"  ~~> [Cypher] $query")
      val data = db.execute(query)
      buildQueryImpact(data.getQueryStatistics)
    } catch{
      case e: Exception => logger.error(e); throw e
      case e: Error => logger.error(e); throw new RuntimeException(e)
    }
  }

  override protected def _exec(query: Query[Cypher]): Stream[Node] = {
    try {
      val q = query.translate.toString
      logger.info(s"  ~~> [Cypher] $q")
      toStream(db.execute(q), query.resultColumn)
    }catch{
      case e: Exception => logger.error(e); throw e
      case e: Error => logger.error(e); throw new RuntimeException(e)
    }
  }


  private def toStream(r: Result, column: String): Stream[Node] = {
    import collection.JavaConverters._
    r.columnAs[org.neo4j.graphdb.Node](column).asScala.toStream map { n =>
      val uuid = java.util.UUID.fromString(n.getProperty(Neo4JHelpers.ACE_UUID).asInstanceOf[String])
      val label = n.getLabels.asScala.toList.head.name()
      val myNode = new Node(uuid,label)
      myNode.fill(n.getAllProperties.asScala.toMap - Neo4JHelpers.ACE_UUID)
      myNode
    }
  }

  private def buildQueryImpact(stats: QueryStatistics): Map[HistoryEvent.Value, Int] = {
    import HistoryEvent._
    Map(
      ConstraintsAdded     -> stats.getConstraintsAdded,
      ConstraintsRemoved   -> stats.getConstraintsRemoved,
      IndexesAdded         -> stats.getIndexesAdded,
      IndexesRemoved       -> stats.getIndexesRemoved,
      LabelsAdded          -> stats.getLabelsAdded,
      LabelsRemoved        -> stats.getLabelsRemoved,
      NodesCreated         -> stats.getNodesCreated,
      NodesDeleted         -> stats.getNodesDeleted,
      PropertiesSet        -> stats.getPropertiesSet,
      RelationshipsCreated -> stats.getRelationshipsCreated,
      RelationshipsDeleted -> stats.getRelationshipsDeleted
    )
  }

}