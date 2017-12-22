package fsm

import fsm.model.ArduinoApp


trait MergeApps {
  def +(that: ArduinoApp[_]): ArduinoApp[_] = ???
}