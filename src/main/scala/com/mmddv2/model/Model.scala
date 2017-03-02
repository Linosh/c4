package com.mmddv2.model

final case class CObject(fields: Map[String, CValue]) {
  def field(k: String): CValue = fields(k)

  def get(k: String): Option[CValue] = fields get k

  def map(f: Map[String, CValue] => Map[String, CValue]): CObject = CObject(f(fields))

  def + (kv: (String, CValue)) = CObject(fields + kv)

  def - (k: String) = CObject(fields - k)
}

object CObject {
  val empty = CObject(Map())

  val cid = "cid"
  val cname = "cname"
  val ctype = "ctype"
  val cparent = "cparent"
}

sealed trait CValue {
  type MValueType
  val value: MValueType
}

sealed abstract class AbstractCValue[TYPE] extends CValue {
  override type MValueType = TYPE
}

final case class IntCValue(value: Int) extends AbstractCValue[Int]
final case class DoubleCValue(value: Double) extends AbstractCValue[Double]
final case class StringCValue(value: String) extends AbstractCValue[String]
final case class ObjectCValue(value: CObject) extends AbstractCValue[CObject]
final case class ListCValue(value: List[CValue]) extends AbstractCValue[List[CValue]]