package eu.ace_design

import java.util.UUID

import scala.language.implicitConversions

import eu.ace_design.engine.graph.VertexIdentifier

package object engine {

  private[engine] final val AceUuid = "_ace_uuid"

  case class EnrichedUUID(uuid: UUID) {
    def :-(label: String): VertexIdentifier = VertexIdentifier(uuid, label)
  }

  case class PartialPropertySetter(vid: VertexIdentifier) {
    case class PropertySetter(vid: VertexIdentifier, propName: String) {
      def :=[T](value: T): SetVertexProperty[T] = SetVertexProperty(vid, propName, value)
    }
    def |(propName: String) = PropertySetter(vid, propName)
  }

  case class EdgeStart(source: VertexIdentifier) {
    case class PartialEdge(source: VertexIdentifier, rel: String) {
      def -->(target: VertexIdentifier): CreateEdge = CreateEdge(source, rel, target)
    }
    def --(rel: String): PartialEdge = PartialEdge(source, rel)
  }

  implicit def uuid2enrichedUuid(uuid: UUID): EnrichedUUID =
    EnrichedUUID(uuid)

  implicit def vertexIdentifier2CreateVertex(vid: VertexIdentifier): CreateVertex =
    CreateVertex(vid)

  implicit def vertexIdentifier2PropertySetter(vid: VertexIdentifier): PartialPropertySetter =
    PartialPropertySetter(vid)

  implicit def vertexIdentifier2EdgeStart(vid: VertexIdentifier): EdgeStart =
    EdgeStart(vid)


}
