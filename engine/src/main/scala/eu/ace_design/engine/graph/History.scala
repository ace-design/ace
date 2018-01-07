package eu.ace_design.engine.graph

import org.neo4j.graphdb.QueryStatistics

import scala.collection.mutable

trait History {

  object EventType extends Enumeration {
    type EventType = Value
    val ConstraintsAdded, ConstraintsRemoved,
        IndexesAdded, IndexesRemoved,
        LabelsAdded, LabelsRemoved,
        NodesCreated, NodesDeleted,
        PropertiesSet,
        RelationshipsCreated, RelationshipsDeleted = Value
  }

  import EventType._

  private class Event(val a: Translatable, val data: Map[EventType, Int])

  private val history: mutable.Buffer[Event] = new mutable.ListBuffer[Event]()

  protected def register(a: Translatable, stats: QueryStatistics): Unit = {
    val recorded = Map(
      ConstraintsAdded -> stats.getConstraintsAdded,
      ConstraintsRemoved -> stats.getConstraintsRemoved,
      IndexesAdded -> stats.getIndexesAdded,
      IndexesRemoved -> stats.getIndexesRemoved,
      LabelsAdded -> stats.getLabelsAdded,
      LabelsRemoved -> stats.getLabelsRemoved,
      NodesCreated -> stats.getNodesCreated,
      NodesDeleted -> stats.getNodesDeleted,
      PropertiesSet -> stats.getPropertiesSet,
      RelationshipsCreated -> stats.getRelationshipsCreated,
      RelationshipsDeleted -> stats.getRelationshipsDeleted
    )
    history += new Event(a, recorded)
  }

  def extract(eventType: EventType): Int =
    (0 /: history.map( e => e.data.getOrElse(eventType, 0) )) { _ + _ }

  def length: Int = history.size

}
