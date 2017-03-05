package com.mmddv2.event

import com.mmddv2.model.CObject

sealed trait CCommand
sealed trait UpdateCCommand extends CCommand
sealed trait ReadCCommand extends CCommand

abstract class CObjectCommand(cObject: CObject) extends UpdateCCommand
/* update commands*/
final case class CreateCObject(cObject: CObject) extends CObjectCommand(cObject)
final case class UpdateCObject(cObject: CObject) extends CObjectCommand(cObject)
final case class DeleteCObject(cObject: CObject) extends CObjectCommand(cObject)
final case class LinkCObject(cObject: CObject, parent: CObject) extends CObjectCommand(cObject)
final case class UnLinkCObject(cObject: CObject) extends CObjectCommand(cObject)

/* read commands*/
final case class FindCObject(id: String) extends ReadCCommand
case object FindAllCObjects extends ReadCCommand

/* stats */
case object PrintStats extends UpdateCCommand