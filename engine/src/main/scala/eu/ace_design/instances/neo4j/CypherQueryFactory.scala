package eu.ace_design.instances.neo4j

import java.util.UUID

import eu.ace_design.kernel.modules.query.{Query, QueryFactory}
import Neo4JHelpers._

object CypherQueryFactory extends QueryFactory[Cypher] {

  import CypherQueries._

  override def findByIdentifier(uuid: UUID, label: String): Query[Cypher] =
    findLike(label, Map(ACE_UUID -> uuid.toString))

  override def findByLabel(label: String): Query[Cypher] =
    findLike(label, Map())

  override def findLike(label: String, props: Map[String, Any]): Query[Cypher] =
    FIND_BY_PROPERTIES(NodeCharacteristics(label, props))

  override def followRelation(uuid: UUID, label: String, relation: String): Query[Cypher] =
    FOLLOW_REL(NodeCharacteristics(label, Map(ACE_UUID -> uuid.toString)), relation)


  object CypherQueries {

    import scala.language.implicitConversions

    case class FIND_BY_PROPERTIES(id: NodeCharacteristics) extends Query[Cypher] {
      override val resultColumn: String = "r"
      override def translate: Cypher =
        s"MATCH ${namedAs(id, resultColumn)} RETURN $resultColumn"
    }

    case class FOLLOW_REL(id: NodeCharacteristics, rel: String) extends Query[Cypher] {
      override val resultColumn: String = "target"
      override def translate: Cypher =
        s"MATCH ${namedAs(id,"src")}-[:$rel]->($resultColumn) RETURN $resultColumn"
    }

  }

}



