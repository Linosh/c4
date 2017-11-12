package c4.lifecycle.common.phase

import c4.lifecycle.LifecyclePhase

/**
  * Created by dmitriiiermiichuk on 7/4/16.
  */
abstract class NotificationPhase[STATE] extends LifecyclePhase[STATE] {
  protected def sendNotification(state: STATE)

  override def execute(state: STATE): STATE = {
    sendNotification(state)
    state
  }
}

class EmailNotificationPhase[STATE](val name:String) extends NotificationPhase[STATE] {
  override def sendNotification(state: STATE) = println("Email Sent")
}
