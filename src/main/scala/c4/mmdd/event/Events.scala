package c4.mmdd.event

import c4.mmdd.model.CObject

sealed trait CCommand
sealed trait UpdateCCommand extends CCommand
sealed trait ReadCCommand extends CCommand

abstract class CObjectCommand(cObject: CObject) extends UpdateCCommand
/* update commands*/
final case class CreateCObject(cObject: CObject) extends CObjectCommand(cObject)
final case class UpdateCObject(cObject: CObject) extends CObjectCommand(cObject)
final case class DeleteCObject(cObject: CObject) extends CObjectCommand(cObject)
final case class ChangeParent(cObject: CObject, parent: CObject) extends CObjectCommand(cObject)

/* read commands*/
final case class FindCObject(id: String) extends ReadCCommand
case object FindAllCObjects extends ReadCCommand

/* stats */
case object PrintStats extends UpdateCCommand