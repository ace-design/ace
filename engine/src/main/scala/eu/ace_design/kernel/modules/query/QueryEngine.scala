package eu.ace_design.kernel.modules.query

import eu.ace_design.kernel.graph.{GraphStorage, Node, Transactional}

trait QueryEngine[L] { self: GraphStorage[L] with Transactional[_] =>

  def handle[R](query: Query[L], callback: Option[Node] => R): R = withinTransaction {
    logger.info(s"  $query")
    _exec(query) match {
      case Stream.Empty => callback(None)
      case head #:: Stream.Empty => callback(Some(head))
      case _ => throw new RuntimeException("More than one result retrieved")
    }
  }

  def handleStream[R](query: Query[L], callback: Stream[Node] => R): R = withinTransaction {
    logger.info(s"  $query")
    callback(_exec(query))
  }

}
