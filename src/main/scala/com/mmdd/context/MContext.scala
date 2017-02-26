package com.mmdd.context

import akka.actor.{ActorSystem, PoisonPill, Props}
import com.mmdd.event.{ReadMCommand, UpdateMCommand}
import com.mmdd.state.{MState, StateReader, StateWriter}

final case class MContext(name: String, state: MState)(implicit actorSystem: ActorSystem) {
  private val stateWriter = actorSystem.actorOf(Props(classOf[StateWriter], state), s"$name-StateManager")
  private val stateReader = StateReader(state)

  def init(): Unit = {}

  def destroy(): Unit = stateWriter ! PoisonPill

  def updateCommand(cmd: UpdateMCommand): Unit = stateWriter ! cmd

  def readCommand[T](cmd: ReadMCommand): T = (stateReader read cmd).asInstanceOf[T]
}
