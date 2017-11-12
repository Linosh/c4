package c4.mmdd.state

import c4.mmdd.model.CObject
import org.scalatest.{FlatSpec, Matchers}

class StateTest extends FlatSpec with Matchers {
  behavior of "C State"

  it should "have NO STATE instance" in {
    import CObject._
    import c4.mmdd.model.CValue._

    val state = NoCState

    assertResult(List())(state.allCObjects())
    assertResult(None)(state.cObject("1"))

    state + CObject.empty
    state + CObject(Map(idAttr -> "1"))
    assertResult(List())(state.allCObjects())

    state - CObject(Map(idAttr -> "1"))
    assertResult(List())(state.allCObjects())
  }
}
