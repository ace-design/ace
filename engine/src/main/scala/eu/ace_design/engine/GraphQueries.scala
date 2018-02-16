package eu.ace_design.engine

import eu.ace_design.engine.graph._
import org.neo4j.graphdb.Result
import org.neo4j.graphdb.Node


trait GraphQuery extends Translatable {
  def transform(raw: Result): Vertex
}

trait MultiGraphQuery extends Translatable {
  def transform(raw: Result): Set[Vertex]
}

case class FindVertexByUUID(vid: VertexIdentifier) extends GraphQuery {
  import scala.collection.JavaConversions._

  override def toCypher: Cypherable = MATCH_NODES(vid.label, Map(AceUuid -> vid.uuid.toString), "result")

  def transform(raw: Result): Vertex = {
    val result = new Vertex()
    val data = raw.next().get("result").asInstanceOf[Node]
    data.getAllProperties.foreach { case (k,v) => result(k) = v }
    result
  }

}