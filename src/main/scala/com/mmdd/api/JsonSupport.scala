package com.mmdd.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.mmdd.context.MContext
import com.mmdd.event.{FindMField, FindMObject}
import com.mmdd.model._
import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsObject, JsString, JsValue, JsonFormat, RootJsonFormat, pimpAny}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  val ctx: MContext

  /**
    *
    */
  implicit val mvFormat: JsonFormat[MValue] = new JsonFormat[MValue] {
    override def write(obj: MValue): JsValue = obj match {
      case IntMValue(v) => JsNumber(v)
      case DoubleMValue(v) => JsNumber(v)
      case StringMValue(v) => JsString(v)
      case ObjectMValue(v) => v.toJson
      case ListMValue(v) => JsArray(v.map(_.toJson).toVector)
    }

    override def read(json: JsValue): MValue = json match {
      case JsNumber(v) => if (v.isWhole()) IntMValue(v.intValue()) else DoubleMValue(v.doubleValue())
      case JsString(v) => StringMValue(v)
      case JsArray(v) => ListMValue(v.map(read).toList)
      case v: JsValue => ObjectMValue(v.convertTo[MObject])
    }
  }

  /**
    *
    */
  implicit val mfFormat: RootJsonFormat[MField] = rootFormat(jsonFormat2(MField))

  /**
    *
    */
  implicit val mfOpFormat: RootJsonFormat[Option[MField]] = new RootJsonFormat[Option[MField]] {
    override def read(json: JsValue) = json match {
      case v: JsObject => Some(v.convertTo[MField])
      case _ => None
    }

    override def write(obj: Option[MField]) = obj match {
      case Some(mf) => mf.toJson
      case None => JsObject.empty
    }
  }

  /**
    *
    */
  implicit val mfMvFormat: JsonFormat[Map[MField, MValue]] = new JsonFormat[Map[MField, MValue]] {
    override def write(obj: Map[MField, MValue]): JsValue =
      JsObject(obj.map { case (f: MField, v: MValue) => f.id -> v.toJson })

    override def read(json: JsValue): Map[MField, MValue] = json match {
      case JsObject(o) => o.collect {
        case (k, v) =>
          ctx.readCommand[Option[MField]](FindMField(k)) match {
            case Some(mf: MField) => mf -> v.convertTo[MValue]
            case None => throw new UnsupportedOperationException(s"Field $k doesn't exist")
          }
      }

      case _ => throw new UnsupportedOperationException(s"Can't parse json: ${json}")
    }

  }

  /**
    *
    */
  implicit val moOpFormat: RootJsonFormat[Option[MObject]] = new RootJsonFormat[Option[MObject]] {
    override def read(json: JsValue) = json match {
      case v: JsObject if v != JsObject.empty => Some(v.convertTo[MObject])
      case _ => None
    }

    override def write(obj: Option[MObject]) = obj match {
      case Some(v) => v.toJson
      case None => JsObject.empty
    }
  }

  /**
    *
    */
  implicit val moFormat: RootJsonFormat[MObject] = new RootJsonFormat[MObject] {
    override def read(json: JsValue): MObject = json match {
      case v: JsObject =>
        val id = v.fields.get("id").getOrElse(JsString.empty).convertTo[String]

        val parentid = v.fields.get("parent").getOrElse(JsString.empty).convertTo[String]
        val parent = if (parentid != "") ctx.readCommand(FindMObject(parentid)).asInstanceOf[Option[MObject]] else None

        val fields = v.fields.get("fields").getOrElse(JsObject.empty).convertTo[Map[MField, MValue]]

        MObject(id, parent, fields)

      case _ => throw new UnsupportedOperationException(s"can't parse $json")
    }

    override def write(obj: MObject) = JsObject(Map(
      "id" -> JsString(obj.id),
      "parent" -> JsString(obj.parent.map(_.id).getOrElse("")),
      "fields" -> obj.mFields.toJson))
  }
}