package com.mmdd.model

import org.scalatest.{FlatSpec, Matchers}

class ModelTest extends FlatSpec with Matchers {

  import CObject._
  import CValue._

  behavior of "C Model"

  it should "not have objects w/o $id" in {
    val obj = CObject(Map())
    a[NoSuchElementException] should be thrownBy {
      obj.id
    }
  }

  it should "have objects with $id" in {
    val obj = CObject(Map(idAttr -> "testId"))

    assertResult("testId")(obj.id)
  }

  it should "accept strings, ints, doubles, lists and another objects as values" in {
    val id = "id"
    val name = "name"
    val int = 10
    val double = 10.10
    val list = List[CValue](id, name, int, double, CObject.empty)

    val obj = CObject(
      Map(idAttr -> id, "name" -> name, "int" -> int, "double" -> double, "list" -> list))

    assertResult(id)(obj.id)
    assertResult(name)(obj("name").value)
    assertResult(int)(obj("int").value)
    assertResult(double)(obj("double").value)
    assertResult(list)(obj("list").value)
  }

  it should "not fail if attr is missing bu simply return no value" in {
    val obj = CObject(Map(idAttr -> "testId"))

    assertResult(NoCValue)(obj("blah"))
  }

  it should "support value object as well as implicit conversions" in {
    val id = StringCValue("id")
    val name = StringCValue("name")
    val int = IntCValue(10)
    val double = DoubleCValue(10.10)
    val list = ListCValue(List[CValue](id, name, int, double, ObjectCValue(CObject.empty)))

    val obj: CObject = CObject(
      Map(idAttr -> id, "name" -> name, "int" -> int, "double" -> double, "list" -> list))

    assertResult(id.value)(obj.id)
    assertResult(name)(obj("name"))
    assertResult(int)(obj("int"))
    assertResult(double)(obj("double"))
    assertResult(list)(obj("list"))
  }

  it should "change object's internal properties" in {
    import CObject._
    import CValue._

    val obj = CObject(Map(idAttr -> "1", "name" -> "J"))
    assertResult("K")((obj + ("name" -> "K")) ("name").value)
  }

  it should "remove object's internal properties" in {
    import CObject._
    import CValue._

    val obj = CObject(Map(idAttr -> "1", "name" -> "J"))
    assertResult(NoCValue)((obj - "name") ("name"))
  }

  it should "for yield object's internal properties" in {
    import CObject._
    import CValue._

    val k = CObject(Map(idAttr -> "K"))
    val j = CObject(Map(idAttr -> "J"))
    val res = for (ek <- k; ej <- j) yield (ek._1 + ej._1) -> (ek._2.value.toString + ej._2.value.toString)

    assertResult(Some(StringCValue("KJ")))(res.get("$id$id"))
  }

  it should "traverse through parent refs" in {
    import CObject._
    import CValue._

    val k = CObject(Map(idAttr -> "K"))
    val j = CObject(Map(idAttr -> "J", parentAttr -> k))

    assertResult("J|K")(j.foldTree(j.id)((prev, o) => prev + "|" + o.id))
  }

  it should "filter object's internal properties" in {
    import CObject._
    import CValue._

    val obj = CObject(Map(idAttr -> "1", "name" -> "J"))
    assertResult(NoCValue)((obj filter { case (k, _) => k == "name" }) (idAttr))
  }

  it should "support implicit conversions for CValue" in {
    import CValue._

    assertResult("J")(identity[String](StringCValue("J")))
    assertResult(1)(identity[Int](IntCValue(1)))
    assertResult(1.1)(identity[Double](DoubleCValue(1.1)))
    assertResult(CObject.empty)(identity[CObject](ObjectCValue(CObject.empty)))
    assertResult(List[CValue]())(identity[List[CValue]](ListCValue(List())))
  }
}
