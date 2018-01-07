package eu.ace_design.engine.graph

import java.util.UUID

import scala.collection.mutable

case class VertexIdentifier(uuid: UUID, label: String)

class Vertex() {

  private val properties: mutable.Map[String, AnyRef] = new mutable.HashMap[String, AnyRef]()

  def apply(key: String) = properties(key)

  def update(key: String, value: AnyRef): Unit = {
    properties(key) = value
  }

}

class Edge() {

}