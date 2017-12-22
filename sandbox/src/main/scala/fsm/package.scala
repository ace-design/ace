import ace.Invariants

package object fsm {

  // Type System
  protected type State = model.State
  protected type App   = model.ArduinoApp[State] with Invariants

  // Instance factory
  object factory {
    def app(n: String): App = ???
    def state(): State = ???
  }

}
