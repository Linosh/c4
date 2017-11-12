package c4.mmdd.state

import c4.mmdd.event.{ChangeParent, CreateCObject, DeleteCObject, UpdateCObject}
import c4.mmdd.model.{CObject, StringCValue}
import org.scalatest.{FlatSpec, Matchers}

class StateWriterTest extends FlatSpec with Matchers {

  behavior of "State Writer"

  it should "create objects" in {
    import CObject._
    import c4.mmdd.model.CValue._

    val state = InMemoryCState()
    val writer = StateWriter(state)

    val obj = CObject(Map(idAttr -> "1"))
    writer.write(CreateCObject(obj))

    assertResult(obj)(writer.state.cObject("1").get)
  }

  it should "update objects" in {
    import CObject._
    import c4.mmdd.model.CValue._

    val state = InMemoryCState()
    val writer = StateWriter(state)

    val obj = CObject(Map(idAttr -> "1", "name" -> "J"))

    writer.write(CreateCObject(obj))
    assertResult(obj)(writer.state.cObject("1").get)
    assertResult(StringCValue("J"))(writer.state.cObject("1").map(_ ("name")).get)

    writer.write(UpdateCObject(obj + ("name" -> "K")))
    assertResult(StringCValue("K"))(writer.state.cObject("1").get("name"))
  }

  it should "delete objects" in {
    import CObject._
    import c4.mmdd.model.CValue._

    val state = InMemoryCState()
    val writer = StateWriter(state)

    val obj = CObject(Map(idAttr -> "1"))

    writer.write(CreateCObject(obj))
    assertResult(obj)(state.cObject("1").get)

    writer.write(DeleteCObject(obj))
    assertResult(None)(state.cObject("1"))
  }

  it should "change parent" in {
    import CObject._
    import c4.mmdd.model.CValue._

    val state = InMemoryCState()
    val writer = StateWriter(state)

    val obj = CObject(Map(idAttr -> "1", parentAttr -> CObject.empty))

    writer.write(CreateCObject(obj))
    assertResult(obj)(state.cObject("1").get)

    writer.write(ChangeParent(obj, obj))
    assertResult(obj)(state.cObject("1").flatMap(_.parent).get)
  }

}
