package com.mmddv2.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.mmddv2.context.CContext
import com.mmddv2.model._
import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsString, JsValue, JsonFormat, RootJsonFormat, pimpAny}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  val ctx: CContext

  implicit val mvFormat: JsonFormat[CValue] = new JsonFormat[CValue] {
    override def write(obj: CValue): JsValue = obj match {
      case IntCValue(v) => JsNumber(v)
      case DoubleCValue(v) => JsNumber(v)
      case StringCValue(v) => JsString(v)
      case ObjectCValue(v) => v.toJson
      case ListCValue(v) => JsArray(v.map(_.toJson).toVector)
    }

    override def read(json: JsValue): CValue = json match {
      case JsNumber(v) => if (v.isWhole()) IntCValue(v.intValue()) else DoubleCValue(v.doubleValue())
      case JsString(v) => StringCValue(v)
      case JsArray(v) => ListCValue(v.map(read).toList)
      case v: JsValue => ObjectCValue(v.convertTo[CObject])
    }
  }

  implicit val moFormat: RootJsonFormat[CObject] = new RootJsonFormat[CObject] {
    override def read(json: JsValue):CObject = CObject(json.convertTo[Map[String, CValue]])

    override def write(obj: CObject): JsValue = obj.fields.toJson
  }
}