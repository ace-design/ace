package eu.ace_design.kernel.engines.storage

case class StorageActionResult(action: StorageAction[_], history: Map[HistoryEvent.Value, Int])
