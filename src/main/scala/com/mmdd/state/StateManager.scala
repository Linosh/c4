package com.mmdd.state

import akka.actor.{ActorSystem, Props}
import com.mmdd.actor.ActorWrapper
import com.mmdd.event.{ReadCCommand, UpdateCCommand}

sealed trait StateManager {
  val stateWriter: StateWriter
  val stateReader: StateReader
  val managerName: String

  def updateCommand(cmd: UpdateCCommand): Unit
  def readCommand[T](cmd: ReadCCommand): T
}

object StateManager {
  def apply(stateWriter: StateWriter,
            stateReader: StateReader,
            managerName: String = "state-manager")(implicit actorSystem: ActorSystem): StateManager = new StateManagerImpl(stateWriter, stateReader, managerName)
}

private class StateManagerImpl(val stateWriter: StateWriter,
                               val stateReader: StateReader,
                               val managerName: String)(implicit actorSystem: ActorSystem) extends StateManager {

  private val writerActor =
    actorSystem.actorOf(
      Props(classOf[ActorWrapper], (e: UpdateCCommand) => stateWriter.write(e)),
      managerName)

  def updateCommand(cmd: UpdateCCommand): Unit = writerActor ! cmd

  def readCommand[T](cmd: ReadCCommand): T = stateReader read cmd
}

