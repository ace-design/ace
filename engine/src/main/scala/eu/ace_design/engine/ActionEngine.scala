package eu.ace_design.engine

import eu.ace_design.engine.graph._
import org.apache.logging.log4j.scala.Logging

import scala.annotation.tailrec


trait ActionEngine extends Logging with History { self: Database =>


  final def execute(actions: GraphAction*): Unit      = withinTransaction { _execute(actions.toList) }
  final def administrate(actions: AdminAction*): Unit = withinTransaction { _execute(actions.toList) }

  @tailrec private def _execute(actions: List[Translatable]): Unit = actions match {
    case Nil =>
    case a :: as => {
      logger.info(s"  $a")
      val statistics = cypher(a.toCypher).getQueryStatistics
      register(a, statistics)
      _execute(as)
    }
  }

}