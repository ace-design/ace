package eu.ace_design.engine


import eu.ace_design.engine.graph._
import org.apache.logging.log4j.scala.Logging
import org.neo4j.graphdb.Result

trait QueryEngine extends Logging { self: Database =>

  final def query(q: GraphQuery): Vertex = withinTransaction {
    logger.info(s"  $q")
    q.transform(cypher(q.toCypher))
  }

}

trait GraphQuery extends Translatable {
  def transform(raw: Result): Vertex
}


/******************************************
  * Available Queries on the Graph Model **
  *****************************************/

case class FindVertexByUUID(vid: VertexIdentifier) extends GraphQuery {
  import scala.collection.JavaConversions._

  override def toCypher: Cypherable = MATCH_NODES(vid.label, Map(AceUuid -> vid.uuid.toString), "result")

  def transform(raw: Result): Vertex = {
    val result = new Vertex()
    val data = raw.next().get("result").asInstanceOf[org.neo4j.graphdb.Node]
    data.getAllProperties.foreach { case (k,v) => result(k) = v }
    result
  }

}