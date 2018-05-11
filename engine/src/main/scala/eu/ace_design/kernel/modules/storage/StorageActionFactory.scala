package eu.ace_design.kernel.modules.storage

import java.util.UUID


trait StorageActionFactory[TargetLanguage] {

  type ProducedAction = StorageAction[TargetLanguage]

  def createNode(uuid: UUID, nodeType: String): ProducedAction

  def setNodeProperty[T](uuid: UUID, nodeType: String, prop: String, value: T): ProducedAction

  def createEdge(source: (UUID, String), relationship: String, target: (UUID, String)): ProducedAction

  // Administration

  def declare(elementType: String): ProducedAction

  def wipe(): ProducedAction

}
