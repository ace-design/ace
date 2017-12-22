package fsm.impl

import ace.{ModelElement, QueryManager}

class StateImpl extends fsm.model.State {
  override def name: String = ???
  override def name_=(n: String): Unit = ???
}

abstract class ArduinoAppImpl extends fsm.model.ArduinoApp[StateImpl] with ModelElement {

  override def initial: StateImpl = ???
  override def initial_=(s: StateImpl): Unit = ???

  override def name: String = ???
  override def name_=(n: String): Unit = ???

}


trait Action