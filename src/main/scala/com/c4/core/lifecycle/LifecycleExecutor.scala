package com.c4.core.lifecycle

/**
  * Created by dmitriiiermiichuk on 6/18/16.
  */
sealed trait LifecycleExecutor[S, L <: Lifecycle[S, _]] {
  def execute(lf: L, state: S): S
}

object LifecycleExecutor {
  def seqExecutor[S] = new SeqExecutor[S]()
}

class SeqExecutor[S] extends LifecycleExecutor[S, SeqLifecycle[S]] {
  override def execute(lf: SeqLifecycle[S], state: S): S =
    lf.phases.foldLeft[S](state)((state, phase) => phase.execute(state))
}
