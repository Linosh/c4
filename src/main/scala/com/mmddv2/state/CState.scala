package com.mmddv2.state

import com.mmddv2.model.{CObject, StringCValue}

sealed trait CState {

  private[state] def allCObjects(): Iterable[CObject]

  private[state] def cObject(id: String): Option[CObject]

  private[state] def +(mo: CObject): Unit

  private[state] def -(mo: CObject): Unit
}

final case class InMemoryCState() extends CState {

  import CObject._

  private var cObjects = Map[String, CObject]()

  override private[state] def allCObjects(): Iterable[CObject] = cObjects.values

  override private[state] def cObject(id: String): Option[CObject] = cObjects.get(id)

  override private[state] def +(mo: CObject): Unit = {
    mo.get(cid) match {
      case Some(StringCValue(key)) => cObjects += (key -> mo)
      case _ => println("cid is not defined")
    }
  }

  override private[state] def -(mo: CObject): Unit = {
    mo.get(cid) match {
      case Some(StringCValue(key)) => cObjects -= key
      case _ => println("cid is not defined")
    }
  }
}