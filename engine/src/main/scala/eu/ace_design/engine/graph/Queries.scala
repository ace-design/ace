package eu.ace_design.engine.graph

import org.neo4j.graphdb.Result

trait Translatable { def toCypher: Cypherable }

sealed trait Cypherable {
  final type Cypher = String
  def translate: Cypher
  def postCondition(res: Result): Boolean = true
}

case class DatabaseNodeIdentifier(label: String, requirements: Map[String,String]) {
  def namedAs(nodeName: String): String = s"($nodeName: $label { ${map2cypher(requirements)} })"
}

case class CREATE_NODE(identifier: DatabaseNodeIdentifier) extends Cypherable {
  override def translate: Cypher = s"CREATE ${identifier.namedAs("n")}"
  override def postCondition(res: Result): Boolean =
    res.getQueryStatistics.getNodesCreated == 1
}

case class CREATE_RELATIONSHIP(relationship: String, source: DatabaseNodeIdentifier, target: DatabaseNodeIdentifier)
  extends Cypherable {
  override def translate: Cypher =
    s"""MATCH
      |   ${source.namedAs("s")}, ${target.namedAs("t")}
      | CREATE
      |   (s)-[:$relationship]->(t)""".stripMargin.replaceAll("\\s+", " ")

  override def postCondition(res: Result): Boolean =
    res.getQueryStatistics.getRelationshipsCreated == 1
}

case class IS_UNIQUE(label: String, property: String) extends Cypherable {
  override def translate: Cypher = {
    s"CREATE CONSTRAINT ON (n:$label) ASSERT n.$property IS UNIQUE"
  }
  override def postCondition(res: Result): Boolean =
    res.getQueryStatistics.getConstraintsAdded == 1
}

case class MATCH_NODES(label: String, properties: Map[String, String], result: String) extends Cypherable {
  override def translate: Cypher = {
    s"MATCH ($result:$label { ${map2cypher(properties)} }) RETURN $result"
  }
}

case class SET(nodeId: DatabaseNodeIdentifier, properties: Map[String, String] ) extends Cypherable {
  override def translate: Cypher = {
    val propSets = properties.map { case (k,v) => s"n.$k = '$v'"}.mkString(", ")
    s"MATCH ${nodeId.namedAs("n")} SET $propSets"
  }
  override def postCondition(res: Result): Boolean =
    res.getQueryStatistics.getPropertiesSet == properties.size
}

case class WIPE() extends Cypherable {
  override def translate: Cypher = "MATCH (n) DETACH DELETE n"
}