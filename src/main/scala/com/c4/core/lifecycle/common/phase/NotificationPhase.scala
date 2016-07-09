package com.c4.core.lifecycle.common.phase

import com.c4.core.lifecycle.{State, LifecyclePhase}

/**
  * Created by dmitriiiermiichuk on 7/4/16.
  */
abstract class NotificationPhase[S <: State[S]] extends LifecyclePhase[S] {

  //override val name: String =

  //override def execute(state: S): S = ???
}
