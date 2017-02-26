package com.mmdd.state

import com.mmdd.event._

final case class StateReader(state: MState) {
  def read(cmd: ReadMCommand) = cmd match {
    case FindMObject(id) => state mObject id
    case FindAllMObjects => state allMObjects

    case FindMField(id) => state mField id
    case FindAllMFields => state allMFields
  }
}
