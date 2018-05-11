package eu.ace_design.instances.neo4j

import java.util.UUID

import eu.ace_design.kernel.modules.storage.{HistoryEvent, StorageAction, StorageActionFactory}

import Neo4JHelpers._

object CypherStorageFactory extends StorageActionFactory[Cypher] {

  import CypherQueries._

  override def createNode(uuid: UUID, nodeType: String) =
    CREATE_NODE(NodeCharacteristics(nodeType, Map(ACE_UUID -> uuid.toString)))

  override def setNodeProperty[T](uuid: UUID, nodeType: String, prop: String, value: T) = value match {
    case b: Boolean =>
      SET(NodeCharacteristics(nodeType, Map(ACE_UUID -> uuid.toString)), Map(prop -> b))
    case l: Long =>
      SET(NodeCharacteristics(nodeType, Map(ACE_UUID -> uuid.toString)), Map(prop -> l))
    case s: String =>
      SET(NodeCharacteristics(nodeType, Map(ACE_UUID -> uuid.toString)), Map(prop -> s))
    case d: Double =>
      SET(NodeCharacteristics(nodeType, Map(ACE_UUID -> uuid.toString)), Map(prop -> d))
    case _ =>
      throw new RuntimeException("Unsupported type for property (must be in [String, Double, Long, Boolean]")
  }

  override def createEdge(source: (UUID, String), relationship: String, target: (UUID, String)) =
    CREATE_EDGE(
      EdgeCharacteristics(relationship,
        NodeCharacteristics(source._2, Map(ACE_UUID -> source._1.toString)),
        NodeCharacteristics(target._2, Map(ACE_UUID -> target._1.toString)))
    )

  override def declare(elementType: String) = IS_UNIQUE(elementType, ACE_UUID)

  override def wipe() = WIPE()

  object CypherQueries {

    import scala.language.implicitConversions

    case class CREATE_NODE(identifier: NodeCharacteristics) extends StorageAction[Cypher] {
      override def translate = s"CREATE ${namedAs(identifier, "n")}"
      override def postCondition(result: Map[HistoryEvent.Value, Int]) =
        result.getOrElse(HistoryEvent.NodesCreated,0) == 1
    }

    case class SET(id: NodeCharacteristics, properties: Map[String, Any]) extends StorageAction[Cypher] {
      override def translate = s"MATCH ${namedAs(id, "n")} SET ${map2cypher(properties, "n."," = ")}"
      override def postCondition(result: Map[HistoryEvent.Value, Int]) =
        result.getOrElse(HistoryEvent.PropertiesSet,0) == properties.size
    }

    case class IS_UNIQUE(nodeType: String, propertyName: String) extends StorageAction[Cypher] {
      override def translate = s"CREATE CONSTRAINT ON (n:$nodeType) ASSERT n.$propertyName IS UNIQUE"
      override def postCondition(result: Map[HistoryEvent.Value, Int]) =
        result.getOrElse(HistoryEvent.ConstraintsAdded, 0) == 1
    }

    case class CREATE_EDGE(edgeIdentifier: EdgeCharacteristics) extends StorageAction[Cypher] {
      override def translate =
        s"""MATCH
           |   ${namedAs(edgeIdentifier.source,"s")}, ${namedAs(edgeIdentifier.source,"t")}
           | CREATE
           |   (s)-[:${edgeIdentifier.edgeType}]->(t)"""
      override def postCondition(result: Map[HistoryEvent.Value, Int]) =
        result.getOrElse(HistoryEvent.RelationshipsCreated, 0) == 1
    }

    case class WIPE() extends StorageAction[Cypher] {
      override def translate: Cypher = "MATCH (n) DETACH DELETE n"
      override def postCondition(result: Map[HistoryEvent.Value, Int]): Boolean = true
    }
  }

}



