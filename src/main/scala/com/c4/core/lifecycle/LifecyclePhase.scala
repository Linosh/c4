package com.c4.core.lifecycle

/**
  * Trait to define each lifecycle step in each flow
  */
trait LifecyclePhase[S] {
  val name: String
  def execute(state: S): S
}

object LifecyclePhase {
  def apply[S](lfName: String, fun: S => S) = new LifecyclePhase[S] {
    override val name: String = lfName

    override def execute(state: S): S = fun(state)
  }
}