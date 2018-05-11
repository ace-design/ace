package eu.ace_design.kernel.modules.storage

import eu.ace_design.kernel.graph.{GraphStorage, Transactional}

import scala.annotation.tailrec
import scala.collection.mutable

trait StorageActionEngine[L] { self: GraphStorage[L] with Transactional[_] =>

  private val _history: mutable.Buffer[StorageActionResult] = new mutable.ListBuffer[StorageActionResult]()

  final def apply(actions: StorageAction[L]*): Unit = withinTransaction { _execute(actions.toList) }

  def howMany(eventType: HistoryEvent.Value): Int =
    (0 /: _history.map( e => e.history.getOrElse(eventType, 0) )) { _ + _ }

  def history: Map[HistoryEvent.Value, Int] = {
    (Map[HistoryEvent.Value, Int]() /: _history) { (acc, elem) =>
      acc ++ elem.history.map { case (k,v) => k -> (acc.getOrElse(k,0) + v)}
    }
  }

  def tick: Int = _history.size

  @tailrec private final def _execute(actions: List[StorageAction[L]]): Unit = actions match {
    case Nil =>
    case a :: as => {
      logger.info(s"  $a")
      _history += execute(a)
      _execute(as)
    }
  }

  /**
    * Execute a given graph action on the underlying storage
    * @param action the action to be executed
    * @return the result of the action, if any
    */
  private def execute(action: StorageAction[L]): StorageActionResult = {
    try {
      val result = _execVoid(action)
      assert(action.postCondition(result), s"Action post-condition violated: [${action.getClass.getSimpleName}]")
      StorageActionResult(action, result)
    } catch{
      case e: Exception => logger.error(e); throw e
      case e: Error => logger.error(e); throw new RuntimeException(e)
    }
  }

}