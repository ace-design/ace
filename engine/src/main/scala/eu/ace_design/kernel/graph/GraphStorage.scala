package eu.ace_design.kernel.graph

import eu.ace_design.kernel.modules.query.Query
import eu.ace_design.kernel.modules.storage.{HistoryEvent, StorageAction, StorageActionResult}
import org.apache.logging.log4j.scala.Logging

/**
  *
  * @tparam TargetLanguage the language used to interact with the underlying graph database
  */
trait GraphStorage[TargetLanguage] extends Logging {

  /**
    * Execute a query written in TargetLanguage in the underlying database. This is engine-specific
    * @param action
    * @return
    */
  protected def _execVoid(action: StorageAction[TargetLanguage]):  Map[HistoryEvent.Value, Int]

  protected def _exec(query: Query[TargetLanguage]): Stream[Node]

}


