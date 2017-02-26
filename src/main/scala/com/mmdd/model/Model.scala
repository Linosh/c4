package com.mmdd.model

final case class MObject(id: String, parent: Option[MObject] = None, mFields: Map[MField, MValue] = Map())

final case class MField(id: String, hidden: Boolean = false)

sealed trait MValue {
  type MValueType
  val value: MValueType
}

sealed abstract class AbstractMValue[TYPE] extends MValue {
  override type MValueType = TYPE
}

final case class IntMValue(value: Int) extends AbstractMValue[Int]
final case class DoubleMValue(value: Double) extends AbstractMValue[Double]
final case class StringMValue(value: String) extends AbstractMValue[String]
final case class ObjectMValue(value: MObject) extends AbstractMValue[MObject]
final case class ListMValue(value: List[MValue]) extends AbstractMValue[List[MValue]]