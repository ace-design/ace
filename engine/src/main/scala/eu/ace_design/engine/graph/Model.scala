package eu.ace_design.engine.graph

import scala.collection.mutable

class Node() {

  private val properties: mutable.Map[String, AnyRef] = new mutable.HashMap[String, AnyRef]()

  def apply(key: String) = properties(key)

  def update(key: String, value: AnyRef): Unit = {
    properties(key) = value
  }

}


