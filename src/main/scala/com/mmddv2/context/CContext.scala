package com.mmddv2.context

import akka.actor.{ActorSystem, PoisonPill, Props}
import com.mmddv2.event.{ReadCCommand, UpdateCCommand}
import com.mmddv2.state.{CState, StateReader, StateWriter}

final case class CContext(name: String, state: CState)(implicit actorSystem: ActorSystem) {
  private val stateWriter = actorSystem.actorOf(Props(classOf[StateWriter], state), s"$name-StateManager")
  private val stateReader = StateReader(state)

  def init(): Unit = {}

  def destroy(): Unit = stateWriter ! PoisonPill

  def updateCommand(cmd: UpdateCCommand): Unit = stateWriter ! cmd

  def readCommand[T](cmd: ReadCCommand): T = (stateReader read cmd).asInstanceOf[T]
}
