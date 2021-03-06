package eu.ace_design.kernel.modules.storage

object HistoryEvent extends Enumeration {
  type Event = Value
  val ConstraintsAdded, ConstraintsRemoved, IndexesAdded, IndexesRemoved, LabelsAdded, LabelsRemoved,
  NodesCreated, NodesDeleted, PropertiesSet, RelationshipsCreated, RelationshipsDeleted = Value
}