package com.c4.core.lifecycle

/**
  * Immutable abstraction which defines specific State and Phases for lifecycle
  */
sealed trait Lifecycle[S <: State, P <: LifecyclePhase[S]] {
  /**
    * Lifecycle name
    */
  val name: String

  /**
    * Immutable traversable set of phases
    */
  val phases: Traversable[P]
}

/**
  * Static companion with defined Lifecycle builders
  */
object Lifecycle {
  def seqLifecycleBuilder[S <: State] = SeqLifecycleBuilder[S]("LifecycleBuilder", Seq())
}

/**
  * Sequential implementation of Lifecycle trait
  *
  * @param name name of lifecycle
  * @param phases seq of phases
  * @tparam S [[com.c4.core.lifecycle.State]] definition
  */
case class SeqLifecycle[S <: State](name: String, phases: Seq[LifecyclePhase[S]]) extends Lifecycle[S, LifecyclePhase[S]]

case class SeqLifecycleBuilder[S <: State](name: String, phases: Seq[LifecyclePhase[S]]) {

  def withPhase(phase: LifecyclePhase[S]) = copy[S](phases = phases :+ phase)

  def withName(name: String) = copy[S](name = name)

  def build(): SeqLifecycle[S] = SeqLifecycle(name, phases)
}