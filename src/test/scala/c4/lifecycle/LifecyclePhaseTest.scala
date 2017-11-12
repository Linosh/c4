package c4.lifecycle

import org.scalatest._

/**
  * Created by dmitriiiermiichuk on 6/13/16.
  */
class LifecyclePhaseTest extends FlatSpec {
  behavior of "Lifecycle Phase"

  it should "have a name" in {
    val lc = LifecyclePhase[MapLikeState[String, String]] ("Test LifecyclePhase", identity)
    assertResult("Test LifecyclePhase")(lc.name)
  }

  it should "have execute method" in {
    val lc = LifecyclePhase[MapLikeState[String, String]] ("Test", state => state.add("a", "b"))
    assertResult("b")(lc.execute(MapLikeState[String, String]()).get("a").get)
  }
}
