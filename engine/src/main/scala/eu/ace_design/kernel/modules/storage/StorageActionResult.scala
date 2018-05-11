package eu.ace_design.kernel.modules.storage

case class StorageActionResult(action: StorageAction[_], history: Map[HistoryEvent.Value, Int])
