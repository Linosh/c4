package c4.lifecycle

import org.scalatest._

/**
  * Created by dmitriiiermiichuk on 6/18/16.
  */
class StateTest extends FlatSpec {
  behavior of "A MapLike State"

  it should "be able to add and get values" in {
    assertResult("v")(MapLikeState().add("k", "v").get("k").get)
  }

  it should "be able to add and remove values" in {
    assertResult(None)(MapLikeState().add("k", "v").remove("k").get("k"))
  }

  it should "be able to map states" in {
    val s = MapLikeState().add("k", "v").map[NotificationState](s => new NotificationState(s.get("k").get))
    assertResult("v")(s.v)
  }
}

class NotificationState(val v:String = "") extends State[NotificationState] {}
