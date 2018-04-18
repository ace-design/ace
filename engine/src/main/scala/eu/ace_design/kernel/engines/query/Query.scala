package eu.ace_design.kernel.engines.query

trait Query[TargetLanguage] {

  val resultColumn: String

  def translate: TargetLanguage

}
