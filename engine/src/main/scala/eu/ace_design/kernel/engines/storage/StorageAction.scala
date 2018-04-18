package eu.ace_design.kernel.engines.storage

trait StorageAction[TargetLanguage] {

  def translate: TargetLanguage

  def postCondition(result: Map[HistoryEvent.Value, Int]): Boolean

}
