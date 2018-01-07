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

  private val _history: mutable.Buffer[Event] = new mutable.ListBuffer[Event]()

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
    _history += new Event(a, recorded)
  }

  def howMany(eventType: EventType): Int =
    (0 /: _history.map( e => e.data.getOrElse(eventType, 0) )) { _ + _ }

  def history: Map[EventType, Int] = {
    (Map[EventType, Int]() /: _history) { (acc, elem) =>
      acc ++ elem.data.map { case (k,v) => k -> (acc.getOrElse(k,0) + v)}
    }
  }

  def length: Int = _history.size

}