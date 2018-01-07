package eu.ace_design.engine

import eu.ace_design.engine.graph._

/*********************************************************
  ** Administration Actions available on the Graph model **
  *********************************************************/

sealed trait AdminAction extends Translatable

case class DeclareLabel(label: String) extends AdminAction {
  override def toCypher: Cypherable = IS_UNIQUE(label, AceUuid)
}

case class ResetModel() extends AdminAction {
  override def toCypher: Cypherable = WIPE()
}

/**************************************************
  ** Regular Actions available on the Graph model **
  **************************************************/

sealed trait GraphAction  extends Translatable

case class CreateVertex(id: VertexIdentifier) extends GraphAction {
  override def toCypher: Cypherable =
    CREATE_NODE(DatabaseNodeIdentifier(id.label, Map(AceUuid -> id.uuid.toString)))
}

case class SetVertexProperty[T](id: VertexIdentifier, property: String, value: T) extends GraphAction {
  override def toCypher: Cypherable =
    SET(DatabaseNodeIdentifier(id.label, Map(AceUuid -> id.uuid.toString)), Map(property -> value.toString))
}

case class CreateEdge(source: VertexIdentifier, relationship: String,  target: VertexIdentifier) extends GraphAction {
  override def toCypher: Cypherable = CREATE_RELATIONSHIP(
    relationship,
    DatabaseNodeIdentifier(source.label, Map(AceUuid -> source.uuid.toString)),
    DatabaseNodeIdentifier(target.label, Map(AceUuid -> target.uuid.toString))
  )
}


