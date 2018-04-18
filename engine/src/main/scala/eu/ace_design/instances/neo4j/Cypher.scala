package eu.ace_design.instances.neo4j

case class Cypher(query: String) {

  override def toString: String = query

}