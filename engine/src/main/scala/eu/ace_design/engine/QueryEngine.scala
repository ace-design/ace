package eu.ace_design.engine

import java.util.UUID

import eu.ace_design.engine.graph._
import org.apache.logging.log4j.scala.Logging
import org.neo4j.graphdb.Result

trait QueryEngine extends Logging { self: Database =>

  final def query(q: GraphQuery): Node = withinTransaction {
    logger.info(s"  $q")
    q.transform(cypher(q.toCypher))
  }

}

trait GraphQuery extends Translatable {
  def transform(raw: Result): Node
}

case class FindVertexByUUID(uuid: UUID, label: String) extends GraphQuery {
  import scala.collection.JavaConversions._

  override def toCypher: Cypherable = MATCH(label, Map("_ace_uuid" -> uuid.toString), "result")

  def transform(raw: Result): Node = {
    val result = new Node()
    val data = raw.next().get("result").asInstanceOf[org.neo4j.graphdb.Node]
    data.getAllProperties.foreach { case (k,v) => result(k) = v }
    result
  }

}