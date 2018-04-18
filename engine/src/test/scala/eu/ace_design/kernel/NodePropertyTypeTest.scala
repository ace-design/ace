package eu.ace_design.kernel

import java.util.UUID

import eu.ace_design.kernel.graph.Node
import org.apache.logging.log4j.scala.Logging
import org.specs2.mutable.SpecificationWithJUnit

import scala.util.Random

class NodePropertyTypeTest extends SpecificationWithJUnit with Logging {

  "Node Properties specification".title
  sequential

  "A Node with Properties" should {

    "accept String as data" in {
      val n = new Node(UUID.randomUUID(), "aLabel")
      val stored = (Random.alphanumeric take 10).mkString
      n("aString") = stored
      val retrieved: String = n("aString")
      retrieved must_== stored
    }

    "accept Double as data" in {
      val n = new Node(UUID.randomUUID(), "aLabel")
      val stored = Random.nextDouble()
      n("aDouble") = stored
      val retrieved: Double = n("aDouble")
      retrieved must_== stored
    }

    "accept Boolean as data" in {
      val n = new Node(UUID.randomUUID(), "aLabel")
      val stored = Random.nextBoolean()
      n("aBoolean") = stored
      val retrieved: Boolean = n("aBoolean")
      retrieved must_== stored
    }

    "accept Long as data" in {
      val n = new Node(UUID.randomUUID(), "aLabel")
      val stored = Random.nextLong()
      n("aLong") = stored
      val retrieved: Long = n("aLong")
      retrieved must_== stored
    }
  }
}


