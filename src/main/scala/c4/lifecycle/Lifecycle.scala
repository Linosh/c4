package c4.lifecycle

/**
  * Immutable abstraction which defines specific State and Phases for lifecycle
  */
sealed trait Lifecycle[S, P <: LifecyclePhase[S]] {
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
  def seqLifecycleBuilder[S] = SeqLifecycleBuilder[S]("LifecycleBuilder", Seq())
}

/**
  * Sequential implementation of Lifecycle trait
  *
  * @param name name of lifecycle
  * @param phases seq of phases
  * @tparam S [[c4.lifecycle.State]] definition
  */
case class SeqLifecycle[S](name: String, phases: Seq[LifecyclePhase[S]]) extends Lifecycle[S, LifecyclePhase[S]]

case class SeqLifecycleBuilder[S](name: String, phases: Seq[LifecyclePhase[S]]) {

  def withPhase(phase: LifecyclePhase[S]) = copy[S](phases = phases :+ phase)

  def withName(name: String) = copy[S](name = name)

  def build(): SeqLifecycle[S] = SeqLifecycle(name, phases)
}