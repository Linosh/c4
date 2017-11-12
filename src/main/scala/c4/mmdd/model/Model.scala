package c4.mmdd.model

import scala.annotation.tailrec

final case class CObject(fields: Map[String, CValue]) {

  import CObject._

  lazy val id: String = fields(idAttr).value.asInstanceOf[String]

  def parent: Option[CObject] = fields.get(parentAttr).map(_.value.asInstanceOf[CObject])

  def apply(k: String): CValue = fields.get(k) match {
    case Some(v) => v
    case None => NoCValue
  }

  def get(k: String): Option[CValue] = fields get k

  def map(f: CField => CField): CObject = CObject(fields.map(f))

  /*def partialMap(f: PartialFunction[CField, CField]) = CObject(fields map (v => f applyOrElse(v, identity[CField])))*/

  def flatMap(f: (CField) => CObject): CObject = CObject(fields.flatMap(kv => f(kv).fields))

  def filter(f: (CField) => Boolean): CObject = CObject(fields.filter(f))

  @tailrec
  def foldTree[B](z: B)(f: (B, CObject) => B): B = {
    parent match {
      case None => z
      case Some(p) => p.foldTree(f(z, p))(f)
    }
  }

  def +(kv: CField): CObject = CObject(fields + kv)

  def -(k: String): CObject = CObject(fields - k)
}

object CObject {
  type CField = (String, CValue)

  val empty: CObject = CObject(Map())
  val idAttr = "$id"
  val parentAttr = "$parent"
}

sealed trait CValue {
  type CValueType
  val value: CValueType
}

sealed abstract class AbstractCValue[TYPE] extends CValue {
  override type CValueType = TYPE
}

final case class IntCValue(value: Int) extends AbstractCValue[Int]

final case class DoubleCValue(value: Double) extends AbstractCValue[Double]

final case class StringCValue(value: String) extends AbstractCValue[String]

final case class ObjectCValue(value: CObject) extends AbstractCValue[CObject]

final case class ListCValue(value: List[CValue]) extends AbstractCValue[List[CValue]]

case object NoCValue extends AbstractCValue[Object] {
  override val value: Object = null
}

object CValue {
  implicit def strToCValue(v: String): CValue = StringCValue(v)

  implicit def intToCValue(v: Int): CValue = IntCValue(v)

  implicit def doubleToCValue(v: Double): CValue = DoubleCValue(v)

  implicit def cObjToCValue(v: CObject): CValue = ObjectCValue(v)

  implicit def cValListToCValue(v: List[CValue]): CValue = ListCValue(v)

  implicit def strCValToStr(v: StringCValue): String = v.value

  implicit def intCValToInt(v: IntCValue): Int = v.value

  implicit def doubleCValToDouble(v: DoubleCValue): Double = v.value

  implicit def cObjCValToObj(v: ObjectCValue): CObject = v.value

  implicit def listCValToList(v: ListCValue): List[CValue] = v.value
}