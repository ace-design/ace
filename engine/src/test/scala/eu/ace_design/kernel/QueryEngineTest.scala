package eu.ace_design.kernel

import java.util.UUID

import eu.ace_design.instances.neo4j.Cypher
import eu.ace_design.kernel.engines.storage.StorageAction
import eu.ace_design.kernel.graph.Node
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.specification.BeforeAfterAll

import scala.util.Random

class QueryEngineTest extends SpecificationWithJUnit with BeforeAfterAll {

  "QueryEngine specification".title
  sequential

  import Context._

  val l13 = "l" + Random.alphanumeric.take(9).mkString
  val l2 = "l" + Random.alphanumeric.take(9).mkString
  var uuid1: UUID = _
  var uuid2: UUID = _
  var uuid3: UUID = _

  def beforeAll() = {
    /**
      *   +-> n2 ---+
      *  /          |
      * n1 ---> n3 <+
      *
      */
    uuid1 = UUID.randomUUID()
    val n1: Seq[StorageAction[Cypher]] = Seq(
      storage.createNode(uuid1, l13),
      storage.setNodeProperty(uuid1, l13, "str", "the value #1"),
      storage.setNodeProperty(uuid1, l13, "bool", true)
    )
    uuid2 = UUID.randomUUID()
    val n2: Seq[StorageAction[Cypher]] = Seq(
      storage.createNode(uuid2, l2),
      storage.setNodeProperty(uuid2, l2, "bool", true),
      storage.setNodeProperty(uuid2, l2, "double", 6.66),
      storage.setNodeProperty(uuid2, l2, "long", 42.toLong)
    )
    uuid3 = UUID.randomUUID()
    val n3: Seq[StorageAction[Cypher]] = Seq(
      storage.createNode(uuid3, l13),
      storage.setNodeProperty(uuid3, l13, "str", "another value #3")
    )
    val edges: Seq[StorageAction[Cypher]] = Seq(
      storage.createEdge((uuid1, l13), "aRelation", (uuid2, l2)),
      storage.createEdge((uuid1, l13), "aRelation", (uuid3, l13)),
      storage.createEdge((uuid2, l2), "anotherRelation", (uuid3, l13))
    )
    val todo = n1 ++ n2 ++ n3 ++ edges
    storageEngine (todo:_*)
  }

  def afterAll() = storageEngine(storage.wipe())

  "The Query Engine" should {

    import Context.query._

    val multiCount: Stream[Node] => Int = { _.size }
    val count: Option[Node] => Int = { o => if (o.isEmpty) 0 else 1 }
    val exists: Option[Node] => Boolean = { opt => opt.isDefined }

    "not throw an exception when returning no results" in {
      val uuid = UUID.randomUUID()
      queryEngine.handle(findByIdentifier(uuid, "aLabel"), count) must not(throwAn[Exception])
    }

    "support the execution of a query with no results" in {
      val uuid = UUID.randomUUID()
      queryEngine.handle(findByIdentifier(uuid, "aLabel"), count) must_== 0
    }

    "support the streamed execution of a query with no results" in {
      val uuid = UUID.randomUUID()
      val howMany = queryEngine.handleStream(findByIdentifier(uuid, "aLabel"), multiCount)
      howMany must_== 0
    }

    "retrieve a single node when asked for" in {
      val res = queryEngine.handle(findByIdentifier(uuid2, l2), _.get)
      res[Long]("long")    must_== 42
      res[Boolean]("bool") must_== true
      res[Double]("double")  must_== 6.66
    }

    "retrieve a stream of node when asked for" in {
      queryEngine.handleStream(findByLabel(l13), multiCount) must_== 2
      queryEngine.handleStream(findByLabel(l2), multiCount) must_== 1
    }

    "refuse to handle a single value when multiple matches" in {
      queryEngine.handle(findByLabel(l13), count) must throwAn[Exception]
    }

    "follows a given relation" in {
      queryEngine.handleStream(followRelation(uuid1, l13, "aRelation"), multiCount) must_== 2
      queryEngine.handleStream(followRelation(uuid2, l2, "anotherRelation"), multiCount) must_== 1
    }

  }

}
