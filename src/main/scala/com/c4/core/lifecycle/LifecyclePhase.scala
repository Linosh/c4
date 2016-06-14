package com.c4.core.lifecycle

/**
  * Trait to define each lifecycle step in each flow
  */
trait LifecyclePhase[T, S <: State] {
  val name: String
  val next: Option[LifecyclePhase[T, S]]
  val previous: Option[LifecyclePhase[T, S]]

  def execute(state: S)
}


