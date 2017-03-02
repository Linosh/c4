package com.mmddv2.state

import com.mmddv2.event._
import com.mmddv2.model.CObject

final case class StateReader(state: CState) {
  def read(cmd: ReadCCommand) = cmd match {
    case FindCObject(id) => state cObject id getOrElse CObject.empty
    case FindAllCObjects => state allCObjects
  }
}
