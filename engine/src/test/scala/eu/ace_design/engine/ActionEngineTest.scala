package eu.ace_design.engine

import java.util.UUID

import eu.ace_design.engine.graph.Database
import org.specs2.mutable.SpecificationWithJUnit


class ActionEngineTest extends SpecificationWithJUnit {

  trait Context extends Database with ActionEngine with QueryEngine

  /** Test Configuration **/

  "Graph Action Engine specification".title
  sequential

  private val context = new { } with Context { }

  "The Graph Action Engine" should {

    "support the execution of the empty sequence" in {
      context.execute() must not(throwAn[Exception])
    }

    "create a node when asked to" in {
      val uuid = UUID.randomUUID()
      context.execute(CreateVertex(uuid, "aLabel")) must not(throwAn[Exception])
      val result = context.query(FindVertexByUUID(uuid, "aLabel"))
      result must not(beNull)
    }

    "support label declaration" in {
      context.administrate(DeclareLabel("aLabel")) must not(throwAn[Exception])
    }

    "reject UUID violations" in {
      context.administrate(DeclareLabel("aLabel"))
      val uuid = UUID.randomUUID()
      context.execute(CreateVertex(uuid, "aLabel"), CreateVertex(uuid, "aLabel")) must throwAn[Exception]
    }

    "support property update for a given vertex" in {
      val uuid = UUID.randomUUID()
      context.execute(
        CreateVertex(uuid, "aLabel"),
        SetVertexProperty(uuid, "aLabel", "aProperty", "aValue")
      ) must not(throwAn[Exception])
      val result = context.query(FindVertexByUUID(uuid, "aLabel"))
      result must not(beNull)
      result("aProperty") must_== "aValue"
    }

  }

}


