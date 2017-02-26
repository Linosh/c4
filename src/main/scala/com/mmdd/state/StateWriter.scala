package com.mmdd.state

import akka.actor.Actor
import com.mmdd.event._

final class StateWriter(state: MState) extends Actor {

  override def receive: Receive = {
    case CreateMObject(mo) => state + mo
    case UpdateMObject(mo) => state + mo
    case DeleteMObject(mo) => state - mo
    case LinkMObject(mo, newParent) => state + mo.copy(parent = Some(newParent))
    case UnLinkMObject(mo) => state + mo.copy(parent = None)

    case CreateMField(mf) => state + mf
    case UpdateMField(mf) => state + mf
    case DeleteMField(mf) => state - mf
  }
}
