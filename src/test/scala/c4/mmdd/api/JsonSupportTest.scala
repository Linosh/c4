package c4.mmdd.api

import c4.mmdd.model.{DoubleCValue, _}
import org.scalatest.{FlatSpec, Matchers}
import spray.json.{JsArray, JsNull, JsNumber, JsObject, JsString}

class JsonSupportTest extends FlatSpec with Matchers {

  behavior of "JSON serializer"

  it should "convert CValue to spray's JsValue vice versa" in {
    val converter = new JsonSupport {}

    assertResult(IntCValue(1))(converter.mvFormat.read(JsNumber(1)))
    assertResult(DoubleCValue(1.1))(converter.mvFormat.read(JsNumber(1.1)))
    assertResult(StringCValue("1"))(converter.mvFormat.read(JsString("1")))
    assertResult(ListCValue(List(IntCValue(1), StringCValue("1")))
    )(converter.mvFormat.read(JsArray(Vector(JsNumber(1), JsString("1")))))
    assertResult(NoCValue)(converter.mvFormat.read(JsNull))

    assertResult(JsNumber(1))(converter.mvFormat.write(IntCValue(1)))
    assertResult(JsNumber(1.1))(converter.mvFormat.write(DoubleCValue(1.1)))
    assertResult(JsString("1"))(converter.mvFormat.write(StringCValue("1")))
    assertResult(JsArray(Vector(JsNumber(1), JsString("1")))
    )(converter.mvFormat.write(ListCValue(List(IntCValue(1), StringCValue("1")))))
    assertResult(JsNull)(converter.mvFormat.write(NoCValue))
  }

  it should "convert JsObject to CObject and vice versa" in {
    import CObject._
    import CValue._

    val converter = new JsonSupport {}

    val jsObject = JsObject(Map(idAttr -> JsString("1"), "int" -> JsNumber(10)))
    val cObj = CObject(Map(idAttr -> "1", "int" -> 10))

    assertResult(cObj)(converter.moFormat.read(jsObject))
    assertResult(jsObject)(converter.moFormat.write(cObj))
  }

}
