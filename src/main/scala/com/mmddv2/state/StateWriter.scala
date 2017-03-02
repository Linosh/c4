package com.mmddv2.state

import akka.actor.Actor
import com.mmddv2.event._
import com.mmddv2.model.ObjectCValue

final class StateWriter(state: CState) extends Actor {

  import com.mmddv2.model.CObject._

  override def receive: Receive = {
    case CreateCObject(mo) => state + mo
    case UpdateCObject(mo) => state + mo
    case DeleteCObject(mo) => state - mo
    case LinkCObject(mo, newParent) => state + (mo + (cparent -> ObjectCValue(newParent)))
    case UnLinkCObject(mo) => state + (mo - cparent)
  }
}
