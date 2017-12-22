package eu.ace_design.engine.graph

trait Translatable { def toCypher: Cypherable }

sealed trait Cypherable {
  final type Cypher = String
  def translate: Cypher
}

case class CREATE(label: String, properties: Map[String, String]) extends Cypherable {
  override def translate: Cypher = {
    val props = properties.map { case (k,v) => s"$k: '$v'"}.mkString(", ")
    s"CREATE (n:$label { $props })"
  }
}

case class IS_UNIQUE(label: String, property: String) extends Cypherable {
  override def translate: Cypher = {
    s"CREATE CONSTRAINT ON (n:$label) ASSERT n.$property IS UNIQUE"
  }
}

case class MATCH(label: String, properties: Map[String, String], result: String) extends Cypherable {
  override def translate: Cypher = {
    val props = properties.map { case (k,v) => s"$k: '$v'"}.mkString(", ")
    s"MATCH ($result:$label { $props }) RETURN $result"
  }
}

case class SET(label: String, requirements: Map[String, String], properties: Map[String, String] ) extends Cypherable {
  override def translate: Cypher = {
    val reqs  = requirements.map { case (k,v) => s"$k: '$v'"}.mkString(", ")
    val props = properties.map { case (k,v) => s"n.$k = '$v'"}.mkString(", ")
    s"MATCH (n:$label { $reqs }) SET $props"
  }
}

case class WIPE() extends Cypherable {
  override def translate: Cypher = "MATCH (n) DETACH DELETE n"
}