package eu.ace_design.kernel.modules.storage
import scala.language.existentials

case class StorageActionResult(action: StorageAction[_], history: Map[HistoryEvent.Value, Int])
