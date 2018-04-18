package eu.ace_design.kernel

import java.util.UUID

import eu.ace_design.kernel.engines.storage.HistoryEvent
import org.specs2.mutable.SpecificationWithJUnit

import scala.util.Random

class HistoryTest  extends SpecificationWithJUnit {

  "Action engine history specification".title
  sequential

  import Context.storageEngine

  "The action engine history" should {
    import Context.storage._

    "stay the same when nothing is done" in {
      val before = storageEngine.history
      val clock = storageEngine.tick
      storageEngine()
      storageEngine.history must_== before
      storageEngine.tick must_== clock
    }

    "record how many constraints are added" in {
      val before = storageEngine.history.getOrElse(HistoryEvent.ConstraintsAdded, 0)
      val extracted = storageEngine.howMany(HistoryEvent.ConstraintsAdded)
      before must_== extracted
      storageEngine(declare("myPrettyLabel"), declare("anotherLabel"))
      storageEngine.history.getOrElse(HistoryEvent.ConstraintsAdded, -1) must_== before + 2
      storageEngine.howMany(HistoryEvent.ConstraintsAdded) must_== extracted + 2
    }

    "record how many nodes are created" in {
      val before = storageEngine.history.getOrElse(HistoryEvent.NodesCreated, 0)
      val extracted = storageEngine.howMany(HistoryEvent.NodesCreated)
      val actions = for (i <- 1 to 10) yield createNode(UUID.randomUUID(), "aLabel")
      storageEngine(actions: _*)
      storageEngine.history(HistoryEvent.NodesCreated) must_== before + actions.length
      storageEngine.howMany(HistoryEvent.NodesCreated) must_== extracted + actions.length
    }

    "record how many relationships are created" in {
      val before = storageEngine.history.getOrElse(HistoryEvent.RelationshipsCreated, 0)
      val extracted = storageEngine.howMany(HistoryEvent.RelationshipsCreated)
      val ids = for (i <- 1 to 10)   yield UUID.randomUUID()
      val vertices = for (id <- ids) yield createNode(id, "aLabel")
      val edges = for(i <- 1 to 100)
        yield createEdge((ids(Random.nextInt(10)), "aLabel"), "aRelation", (ids(Random.nextInt(10)), "aLabel"))
      storageEngine(vertices ++ edges: _*)
      storageEngine.history(HistoryEvent.RelationshipsCreated) must_== before + edges.length
      storageEngine.howMany(HistoryEvent.RelationshipsCreated) must_== extracted + edges.length
    }
  }

}
