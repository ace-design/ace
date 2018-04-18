package eu.ace_design.instances.neo4j

object Neo4JHelpers {

  import scala.language.implicitConversions

  val ACE_UUID: String = "_ace_uuid"

  case class NodeCharacteristics(nodeType: String, props: Map[String,Any])
  case class EdgeCharacteristics(edgeType: String, source: NodeCharacteristics, target: NodeCharacteristics)

  implicit def string2cypher(s: String): Cypher = Cypher(s.stripMargin.replaceAll("\\s+", " "))

  def map2cypher(props: Map[String, Any], prefix: String = "", sep: String = ":"): String =
    props.map { case (k, v) =>
      val data = v match {
        case s: String => s"'$s'"
        case _ => v.toString
      }
      s"$prefix$k$sep $data"
    }.mkString(", ")


  def namedAs(id: NodeCharacteristics, n: String): String =
    s"($n: ${id.nodeType} { ${map2cypher(id.props)} })"

}
