package com.mmdd.event

import com.mmdd.model.{MField, MObject}

sealed trait MCommand
sealed trait UpdateMCommand extends MCommand
sealed trait ReadMCommand extends MCommand

abstract class MObjectCommand(mObject: MObject) extends UpdateMCommand
/* update commands*/
final case class CreateMObject(mObject: MObject) extends MObjectCommand(mObject)
final case class UpdateMObject(mObject: MObject) extends MObjectCommand(mObject)
final case class DeleteMObject(mObject: MObject) extends MObjectCommand(mObject)
final case class LinkMObject(mObject: MObject, parent: MObject) extends MObjectCommand(mObject)
final case class UnLinkMObject(mObject: MObject) extends MObjectCommand(mObject)

abstract class MFieldCommand(mf: MField) extends UpdateMCommand
/* update commands*/
final case class CreateMField(mf: MField) extends MFieldCommand(mf)
final case class UpdateMField(mf: MField) extends MFieldCommand(mf)
final case class DeleteMField(mf: MField) extends MFieldCommand(mf)

/* stats */
case object PrintStats extends UpdateMCommand


/* read commands*/
final case class FindMObject(id: String) extends ReadMCommand
case object FindAllMObjects extends ReadMCommand

final case class FindMField(id: String) extends ReadMCommand
case object FindAllMFields extends ReadMCommand