package eu.ace_design.engine


import eu.ace_design.engine.graph._
import org.apache.logging.log4j.scala.Logging

trait QueryEngine extends Logging { self: Database =>

  final def query(q: GraphQuery): Vertex = withinTransaction {
    logger.info(s"  $q")
    q.transform(cypher(q.toCypher))
  }

  final def multiQuery(q: MultiGraphQuery): Set[Vertex] = withinTransaction {
    logger.info(s"  $q")
    q.transform(cypher(q.toCypher))
  }
}