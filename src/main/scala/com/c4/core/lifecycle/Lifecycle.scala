package com.c4.core.lifecycle

/**
  * Trait to define each lifecycle step in each flow
  */
trait Lifecycle[T] {
  val name: String
  val next: Option[Lifecycle[T]]
  val previous: Option[Lifecycle[T]]

  def execute(state: State)
}


