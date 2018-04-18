package eu.ace_design.kernel.graph

import java.util.UUID

import scala.collection.mutable

// Hyp: a node has one single label, even if backend support multiple labels
class Node(val id: UUID, val label: String) {

  private val properties: mutable.Map[String, Any] = new mutable.HashMap[String, Any]()

  def apply[T](key: String): T = properties(key).asInstanceOf[T]

  def update[T](key: String, value: T): Unit = { properties(key) = value }

  def size: Int = properties.size

  def fill(props: Map[String, Any]): Unit = {
    props foreach { case (k,v) => update(k,v) }
  }

}

