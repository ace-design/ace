package eu.ace_design.engine

import java.util.UUID

import eu.ace_design.engine.graph._
import org.apache.logging.log4j.scala.Logging

import scala.annotation.tailrec


trait ActionEngine extends Logging { self: Database =>

  final def execute(actions: GraphAction*): Unit = withinTransaction {
    _execute(actions.toList)
  }

  final def administrate(actions: AdminAction*): Unit = withinTransaction {
    _execute(actions.toList)
  }

  @tailrec private def _execute(actions: List[Translatable]): Unit = actions match {
    case Nil =>
    case a :: as => {
      logger.info(s"  $a")
      cypher(a.toCypher)
      _execute(as)
    }
  }
}

sealed trait AdminAction extends Translatable
sealed trait GraphAction  extends Translatable

case class DeclareLabel(label: String) extends AdminAction {
  override def toCypher: Cypherable =
    IS_UNIQUE(label, "_ace_uuid")
}

case class CreateVertex(uuid: UUID, label: String) extends GraphAction {
  override def toCypher: Cypherable =
    CREATE(label, Map("_ace_uuid" -> uuid.toString))
}

case class SetVertexProperty[T](uuid: UUID, label: String, property: String, value: T) extends GraphAction {
  override def toCypher: Cypherable =
    SET(label, Map("_ace_uuid" -> uuid.toString), Map(property -> value.toString))
}
