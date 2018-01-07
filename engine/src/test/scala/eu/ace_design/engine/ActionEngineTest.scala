package eu.ace_design.engine

import java.util.UUID

import org.specs2.mutable.SpecificationWithJUnit


class ActionEngineTest extends SpecificationWithJUnit {

  /** Test Configuration **/

  "Graph Action Engine specification".title
  sequential

  "The Graph Action Engine" should {

    "support the execution of the empty sequence" in {
      Universe.execute() must not(throwAn[Exception])
    }

    "support label declaration" in {
      Universe.administrate(DeclareLabel("aSpecificLabel")) must not(throwAn[Exception])
    }

    "reject UUID violations" in {
      Universe.administrate(DeclareLabel("aLabel"))
      val uuid = UUID.randomUUID()
      Universe.execute(uuid :- "aLabel", uuid :- "aLabel") must throwAn[Exception]
    }

    "create a node when asked to" in {
      val uuid = UUID.randomUUID()
      Universe.execute(uuid :- "aLabel") must not(throwAn[Exception])
      val result = Universe.query(FindVertexByUUID(uuid :- "aLabel"))
      result must not(beNull)
    }

    "support property update for a given vertex" in {
      val uuid = UUID.randomUUID()
      Universe.execute(
        uuid :- "aLabel",
        uuid :- "aLabel" | "aProperty" := "aValue"
      ) must not(throwAn[Exception])
      val result = Universe.query(FindVertexByUUID(uuid :- "aLabel"))
      result must not(beNull)
      result("aProperty") must_== "aValue"
    }

    "throw an exception when asked to create an edge between to non-existing vertices" in {
      val source  = UUID.randomUUID()
      val target = UUID.randomUUID()
      Universe.execute(
        (source :- "aLabel") -- "aRelation" --> (target :- "aLabel")
      ) must throwAn[Exception]

    }

    "support the definition of edges between vertices" in {
      val source = UUID.randomUUID()
      val target = UUID.randomUUID()

      Universe.execute(
        source :- "aLabel",
        target :- "aLabel",
        (source :- "aLabel") -- "aRelation" --> (target :- "aLabel")
      )

      true must beTrue

    }

  }

}


