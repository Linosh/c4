package c4.mmdd.state

import c4.mmdd.model.CObject

sealed trait CState {

  private[state] def allCObjects(): Iterable[CObject]

  private[state] def cObject(id: String): Option[CObject]

  private[state] def +(mo: CObject): Unit

  private[state] def -(mo: CObject): Unit
}

case object NoCState extends CState {
  override private[state] def allCObjects() = List()

  override private[state] def cObject(id: String) = None

  override private[state] def +(mo: CObject) {}

  override private[state] def -(mo: CObject) {}
}

final case class InMemoryCState() extends CState {

  private var cObjects = Map[String, CObject]()

  override private[state] def allCObjects(): Iterable[CObject] = cObjects.values

  override private[state] def cObject(id: String): Option[CObject] = cObjects.get(id)

  override private[state] def +(mo: CObject): Unit = cObjects += (mo.id -> mo)

  override private[state] def -(mo: CObject): Unit = cObjects -= mo.id
}