package com.mmdd.state

import com.mmdd.event.{FindAllCObjects, FindCObject}
import com.mmdd.model.CObject
import org.scalatest.{FlatSpec, Matchers}

class StateReaderTest extends FlatSpec with Matchers {
  behavior of "State Reader"

  it should "look for object by id" in {
    import CObject._
    import com.mmdd.model.CValue._

    val obj = CObject(Map(idAttr -> "1"))
    val state = InMemoryCState()
    state + obj

    val reader = StateReader(state)

    assertResult(obj)(reader.read[CObject](FindCObject("1")))
  }

  it should "look for object by id and return empty object if nothing found" in {
    val reader = StateReader(NoCState)
    assertResult(CObject.empty)(reader.read[CObject](FindCObject("1")))
  }

  it should "look for all objects" in {
    import CObject._
    import com.mmdd.model.CValue._

    val obj1 = CObject(Map(idAttr -> "1"))
    val obj2 = CObject(Map(idAttr -> "2"))
    val state = InMemoryCState()
    state + obj1
    state + obj2

    val reader = StateReader(state)

    assertResult(2)(reader.read[Iterable[CObject]](FindAllCObjects).size)
  }
}
