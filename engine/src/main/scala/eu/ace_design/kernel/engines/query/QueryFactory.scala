package eu.ace_design.kernel.engines.query

import java.util.UUID

trait QueryFactory[TargetLanguage] {

  type ProducedQuery = Query[TargetLanguage]

  def findByLabel(label: String): ProducedQuery

  def findByIdentifier(uuid: UUID, label: String): ProducedQuery

  def findLike(label: String, props: Map[String, Any]): ProducedQuery

  def followRelation(uuid: UUID, label: String, relation: String): ProducedQuery

}
