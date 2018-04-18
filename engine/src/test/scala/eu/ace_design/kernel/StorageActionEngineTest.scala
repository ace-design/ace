package eu.ace_design.kernel

import java.util.UUID

import org.specs2.mutable.SpecificationWithJUnit

import scala.util.Random

class StorageActionEngineTest extends SpecificationWithJUnit {

  /** Test Configuration **/

  "Storage Graph Action Engine specification".title
  sequential

  import Context.storageEngine

  "The storage Graph Action Engine" should {

    import Context.storage._

    "support the execution of the empty sequence" in {
      storageEngine() must not(throwAn[Exception])
    }

    "support label declaration" in {
      storageEngine(declare("aSpecificLabel")) must not(throwAn[Exception])
    }

    "create a node when asked to" in {
      val uuid = UUID.randomUUID()
      storageEngine(createNode(uuid, "aLabel")) must not(throwAn[Exception])
    }

    "reject UUID violations" in {
      val uuid = UUID.randomUUID()
      storageEngine(declare("aLabel"))
      storageEngine(createNode(uuid, "aLabel"), createNode(uuid, "aLabel")) must throwAn[Exception]
    }

    "support the definition of edges between vertices" in {
      val source = UUID.randomUUID()
      val target = UUID.randomUUID()
      storageEngine(createNode(source, "aLabel"), createNode(target, "aLabel"),
              createEdge((source, "aLabel"), "aRelation", (target, "aLabel"))) must not(throwAn[Exception])
    }

    "throw an exception when asked to create an edge between to non-existing vertices" in {
      val source  = UUID.randomUUID()
      val target = UUID.randomUUID()
      storageEngine(createEdge((source, "aLabel"), "aRelation", (target, "aLabel"))) must throwAn[Exception]
    }

    "throw an exception when setting a property to an unsupported type" in {
      setNodeProperty(UUID.randomUUID(), "aLabel", "aKey", 3) must throwAn[Exception]
    }

    "retrieve a single node when asked for" in {
      val uuid = UUID.randomUUID()
      val long = Random.nextLong()
      val string = (Random.alphanumeric take 10).mkString
      val bool: Boolean = Random.nextBoolean()
      val double = Random.nextDouble()
      storageEngine (
        createNode(uuid, "aLabel"),
        setNodeProperty(uuid, "aLabel", "strVal", string),
        setNodeProperty(uuid, "aLabel", "longVal", long),
        setNodeProperty(uuid, "aLabel", "boolVal", bool),
        setNodeProperty(uuid, "aLabel", "doubleVal", double)
      ) must not(throwAn[Exception])
    }

  }

}
