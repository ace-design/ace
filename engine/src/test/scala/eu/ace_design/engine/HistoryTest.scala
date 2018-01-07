package eu.ace_design.engine

import java.util.UUID
import scala.util.Random

import org.specs2.mutable.SpecificationWithJUnit

class HistoryTest extends SpecificationWithJUnit {

  "History feature specification".title
  sequential

  import Universe.EventType._

  "The History feature" should {

    "stay the same when nothing is done" in {
      val before = Universe.history
      val length = Universe.length
      Universe.execute()
      Universe.history must_== before
      Universe.length must_== length
    }

    "record how many constraints are added" in {
      val before = Universe.history.getOrElse(ConstraintsAdded, 0)
      val extracted = Universe.howMany(ConstraintsAdded)
      Universe.administrate(DeclareLabel("myPrettyLabel"), DeclareLabel("anotherLabel"))
      Universe.history(ConstraintsAdded) must_== before + 2
      Universe.howMany(ConstraintsAdded) must_== extracted + 2
    }

    "record how many nodes are created" in {
      val before = Universe.history.getOrElse(NodesCreated, 0)
      val extracted = Universe.howMany(NodesCreated)
      val actions = for (i <- 1 to 10) yield CreateVertex(UUID.randomUUID() :- "aLabel")
      Universe.execute(actions: _*)
      Universe.history(NodesCreated) must_== before + actions.length
      Universe.howMany(NodesCreated) must_== extracted + actions.length
    }

    "record how many relationships are created" in {
      val before = Universe.history.getOrElse(RelationshipsCreated, 0)
      val extracted = Universe.howMany(RelationshipsCreated)
      val vertices = for (i <- 1 to 10) yield CreateVertex(UUID.randomUUID() :- "aLabel")
      val edges = for(i <- 1 to 100)
        yield CreateEdge(vertices(Random.nextInt(10)).id, "aRelation",vertices(Random.nextInt(10)).id)
      Universe.execute(vertices ++ edges: _*)
      Universe.history(RelationshipsCreated) must_== before + edges.length
      Universe.howMany(RelationshipsCreated) must_== extracted + edges.length
    }
  }

}
