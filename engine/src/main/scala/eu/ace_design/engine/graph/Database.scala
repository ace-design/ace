package eu.ace_design.engine.graph

import java.io.File

import org.apache.logging.log4j.scala.Logging
import org.neo4j.graphdb.factory.GraphDatabaseFactory
import org.neo4j.graphdb.{GraphDatabaseService, Result}


trait Database extends Logging {

  // Locating the Graph Database somewhere on the disk
  private val databaseDirectory = new File(System.getProperty("java.io.tmpdir") + "neo4j")

  // when shutting down the application, stop the graph database engine and destroy the data
  private def registerShutdownHook(graphDb: GraphDatabaseService): Unit = {
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        graphDb.shutdown()
        org.neo4j.io.fs.FileUtils.deleteRecursively(databaseDirectory)
      }
    })
  }

  // Initialising the graph engine
  private val database: GraphDatabaseService = new GraphDatabaseFactory()
    .newEmbeddedDatabaseBuilder(databaseDirectory)
    .newGraphDatabase()

  registerShutdownHook(database)

  protected def cypher(query: Cypherable): Result = {
    val q = query.translate
    logger.info(s"    $q")
    try { database.execute(q) }
    catch{ case e: Exception => logger.error(e); throw e }
  }

  // Execute code within a transaction inside the engine
  protected def withinTransaction[T](code: => T): T = {
    val tx = database.beginTx
    logger.info(s"Beginning Transaction [${tx.hashCode()}]")
    try {
      val result = code
      tx.success()
      result
    }
    finally {
      if (tx != null) tx.close()
      logger.info(s"Closing Transaction [${tx.hashCode()}]")
    }
  }

}
