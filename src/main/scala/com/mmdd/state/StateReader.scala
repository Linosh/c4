package com.mmdd.state

import com.mmdd.event._
import com.mmdd.model.CObject

sealed trait StateReader {
  private[state] val state: CState

  def read[T](cmd: ReadCCommand): T
}

object StateReader {
  def apply(state: CState): StateReader = new StateReaderImpl(state)

  // JUST FOR TESTING PURPOSES
  private[state] def apply(cState: CState, f: ReadCCommand => Any): StateReader = new StateReader {
    override def read[T](cmd: ReadCCommand) = f(cmd).asInstanceOf[T]

    override private[state] val state = cState
  }
}

private final class StateReaderImpl(private[state] val state: CState) extends StateReader {

  def read[T](cmd: ReadCCommand): T = cmd match {
    case FindCObject(id) => (state cObject id getOrElse CObject.empty).asInstanceOf[T]
    case FindAllCObjects => (state allCObjects).asInstanceOf[T]
  }
}
