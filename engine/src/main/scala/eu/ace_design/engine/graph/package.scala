package eu.ace_design.engine

package object graph {

  def map2cypher(properties: Map[String,String]): String =
    properties.map { case (k,v) => s"$k: '$v'"}.mkString(", ")

}
