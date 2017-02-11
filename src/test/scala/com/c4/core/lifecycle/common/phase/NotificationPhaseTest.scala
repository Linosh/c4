package com.c4.core.lifecycle.common.phase

import com.c4.core.lifecycle.KeyValueState
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
