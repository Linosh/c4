package c4.lifecycle.common.phase

import c4.lifecycle.KeyValueState
import org.scalatest.FlatSpec

/**
  * Created by dy on 7/9/16.
  */
class NotificationPhaseTest extends FlatSpec {

  behavior of "NotificationPhase"

  it should "be able to send notification" in {

    val np = new EmailNotificationPhase[KeyValueState[String, String]]("EmptyEmail")
    np.execute(null)
  }

}
