package com.mmdd.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.mmdd.model._
import spray.json.{DefaultJsonProtocol, JsArray, JsNull, JsNumber, JsString, JsValue, JsonFormat, RootJsonFormat, pimpAny}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val mvFormat: JsonFormat[CValue] = new JsonFormat[CValue] {
    override def write(obj: CValue): JsValue = obj match {
      case IntCValue(v) => JsNumber(v)
      case DoubleCValue(v) => JsNumber(v)
      case StringCValue(v) => JsString(v)
      case ListCValue(v) => JsArray(v.map(_.toJson).toVector)
      case _ => JsNull
    }

    override def read(json: JsValue): CValue = json match {
      case JsNumber(v) => if (v.isWhole()) IntCValue(v.intValue()) else DoubleCValue(v.doubleValue())
      case JsString(v) => StringCValue(v)
      case JsArray(v) => ListCValue(v.map(read).toList)
      case _ => NoCValue
    }
  }

  implicit val moFormat: RootJsonFormat[CObject] = new RootJsonFormat[CObject] {
    override def read(json: JsValue):CObject = CObject(json.convertTo[Map[String, CValue]])

    override def write(obj: CObject): JsValue = obj.fields.toJson
  }
}