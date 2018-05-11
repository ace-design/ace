package eu.ace_design.kernel.modules.query

trait Query[TargetLanguage] {

  val resultColumn: String

  def translate: TargetLanguage

}
