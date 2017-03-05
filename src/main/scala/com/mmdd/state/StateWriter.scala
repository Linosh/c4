package com.mmdd.state

import akka.actor.Actor
import com.mmdd.event.{PrintStats, _}

final class StateWriter(state: MState) extends Actor {
  private val startTime = System.currentTimeMillis()
  private var updatesCnt = 0

  override def receive: Receive = {
    case v =>
      updatesCnt += 1
      v match {
        case CreateMObject(mo) => state + mo
        case UpdateMObject(mo) => state + mo
        case DeleteMObject(mo) => state - mo
        case LinkMObject(mo, newParent) => state + mo.copy(parent = Some(newParent))
        case UnLinkMObject(mo) => state + mo.copy(parent = None)

        case CreateMField(mf) => state + mf
        case UpdateMField(mf) => state + mf
        case DeleteMField(mf) => state - mf

        case PrintStats => printStats()
      }
  }

  private def printStats() = {
    println(s"State updates: $updatesCnt")
    println(s"Per second: ${updatesCnt / ((System.currentTimeMillis() - startTime) / 1000)}")
  }
}
