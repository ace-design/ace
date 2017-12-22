package fsm.model

/**
  * val model =
  * model("FSM") {
  *   classifier("NamedElement") -extends-> "Object" {
  *         isAbstract
  *
  *   }
  * }
  *
  * val model =
  * model("FSM")
  *   .withClassifier("NamedElement")
  *     .withProperty("name", "String")
  *   .end()
  *   .withClassifier("App")
  *     .extending("NamedElement")
  *   .end()
  *   .withClassifier("State")
  *   .end()
  * .end()
  */

object SIGNAL extends Enumeration {
  type Signal = Value
  val HIGH, LOW = Value
}
import SIGNAL._


trait NamedElement {
  def name: String
  def name_=(n: String)
}

trait ValuedElement {
  def value: Signal
  def value_=(v: Signal)
}

trait ArduinoApp[_State <: State] extends NamedElement {
  def initial: _State
  def initial_=(s: _State)
}
trait State extends NamedElement

trait Brick extends NamedElement {
  def pin: Integer
  def pin_=(p: Integer)
}

trait Transition extends ValuedElement {
  def sensor: Sensor
  def sensor_=(s: Sensor)
}

trait Effect extends ValuedElement {
  def actuator: Actuator
  def actuator_=(a: Actuator)
}

trait Sensor extends Brick
trait Actuator extends Brick