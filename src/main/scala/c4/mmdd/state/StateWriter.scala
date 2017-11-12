package c4.mmdd.state

import c4.mmdd.event._
import c4.mmdd.model.CObject._
import c4.mmdd.model.ObjectCValue

sealed trait StateWriter {
  private[state] val state: CState

  def write(cmd: UpdateCCommand): Unit
}

object StateWriter {
  def apply(state: CState): StateWriter = new StateWriterImpl(state)

  // JUST FOR TESTING PURPOSES
  private[state] def apply(cState: CState, f: UpdateCCommand => Unit): StateWriter = new StateWriter {
    override def write(cmd: UpdateCCommand) = f(cmd)

    override private[state] val state = cState
  }
}

private final class StateWriterImpl(private[state] val state: CState) extends StateWriter {

  def write(cmd: UpdateCCommand): Unit = cmd match {
    case CreateCObject(mo) => state + mo
    case UpdateCObject(mo) => state + mo
    case DeleteCObject(mo) => state - mo
    case ChangeParent(mo, newParent) => state + (mo + (parentAttr -> ObjectCValue(newParent)))
  }
}
