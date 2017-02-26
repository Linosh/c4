package com.mmdd.state

import com.mmdd.model.{MField, MObject}

sealed trait MState {

  private[state] def allMObjects(): Iterable[MObject]

  private[state] def mObject(id: String): Option[MObject]

  private[state] def allMFields(): Iterable[MField]

  private[state] def mField(id: String): Option[MField]

  private[state] def +(mo: MObject): Unit

  private[state] def +(mo: MField): Unit

  private[state] def -(mo: MObject): Unit

  private[state] def -(mo: MField): Unit
}

final case class InMemoryMState() extends MState {
  private var mObjects = Map[String, MObject]()
  private var mFields = Map[String, MField]()

  override private[state] def allMObjects(): Iterable[MObject] = mObjects.values

  override private[state] def mObject(id: String): Option[MObject] = mObjects.get(id)

  override private[state] def allMFields() = mFields.values

  override private[state] def mField(id: String) = mFields.get(id)

  override private[state] def +(mo: MObject): Unit = mObjects += (mo.id -> mo)

  override private[state] def -(mo: MObject): Unit = mObjects -= mo.id

  override private[state] def +(mf: MField) = mFields += (mf.id -> mf)

  override private[state] def -(mf: MField) = mFields -= mf.id
}