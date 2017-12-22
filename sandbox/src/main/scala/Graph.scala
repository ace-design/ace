import java.io.File

import org.neo4j.graphdb.factory.GraphDatabaseFactory
import org.neo4j.graphdb.{GraphDatabaseService, Transaction}

import org.apache.logging.log4j.scala.Logging




object TestGraph extends App with Logging {

  val databaseDirectory = new File(System.getProperty("java.io.tmpdir") + "neo4j")
  org.neo4j.io.fs.FileUtils.deleteRecursively(databaseDirectory)

  println(databaseDirectory)
  val db = new GraphDatabaseFactory()
    .newEmbeddedDatabaseBuilder(databaseDirectory)
    .newGraphDatabase()
  registerShutdownHook(db)
  logger.info("Creating node (vanilla style)")

  call {
    val myNode = db.createNode
    myNode.setProperty("name", "my node")
  }

  call {
    val res = db.execute("MATCH (n) RETURN n")
    println(res)
  }


//  val driver = GraphDatabase.driver("")
  //println(driver)
  //val tx = driver.session().beginTransaction()



  /*
  println("using storm-cypher")

  implicit val system = ActorSystem("anormcypher")
  implicit val materializer = ActorMaterializer()
  implicit val wsclient = ning.NingWSClient()
  implicit val connection: Neo4jConnection = Neo4jREST()
  implicit val ec = scala.concurrent.ExecutionContext.global
  Cypher("""create (anorm:anormcyphertest {name:"AnormCypher"}), (test:anormcyphertest {name:"Test"})""").execute()

  val req = Cypher("match (n:anormcyphertest) return n.name")
  val stream = req()
  stream.map(row => {row[String]("n.name")}).toList
  Cypher("match (n:anormcyphertest) delete n")()
  wsclient.close()

*/


  def call[T](code: => T): T = {
    val tx = db.beginTx
    try {
      val result = code
      tx.success()
      result
    } finally {
      if (tx != null)
        tx.close()
    }
  }



}
