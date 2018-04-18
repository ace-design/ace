package eu.ace_design.kernel

import java.util.UUID

import eu.ace_design.kernel.graph.Node
import org.specs2.mutable.SpecificationWithJUnit

class NodeTest extends SpecificationWithJUnit {

  "Node specification".title

  "A Node retrieved from the engine" should {

    "be empty at the beginning" in {
      val n: Node = new Node(UUID.randomUUID(), "aLabel")
      n.size must_== 0
    }

    "support property update" in {
      val n: Node = new Node(UUID.randomUUID(), "aLabel")
      n("aProperty") = "foobar"
      n("anotherProperty") = 42
      n.size must_== 2
      n("aProperty") = "geek"
      n.size must_== 2
    }

    "support property retrieval" in {
      val n: Node = new Node(UUID.randomUUID(), "aLabel")
      n("aProperty") = "foobar"
      n[String]("aProperty") must_== "foobar"
      n("anotherProperty") = 42
      n[Int]("anotherProperty") must_== 42
    }

    "support property update" in {
      val n: Node = new Node(UUID.randomUUID(), "aLabel")
      n("aProperty") = "foobar"
      n[String]("aProperty") must_== "foobar"
      n("aProperty") = "geek"
      n[String]("aProperty") must_== "geek"
    }

    "throw an exception when asked to retrieved an unknown property" in {
      val n: Node = new Node(UUID.randomUUID(), "aLabel")
      n[Any]("unknown") must throwAn[Exception]
    }

  }

}
