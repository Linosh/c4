package com.c4.core.lifecycle

import org.scalatest._

/**
  * Created by dmitriiiermiichuk on 6/13/16.
  */
class LifecycleSpec extends FlatSpec {
  "A lifecycle" should "have execute method" in {
    val lc = new Lifecycle[String] {
      override val name: String = "Test"

      override def execute(state: State[String]): Unit = {
        println(state.get())
      }

      override val next = None
      override val previous = None
    }

    val state = new State[String] {
      override def get(): String = "Hello World"

      override def update(t: String): State[String] = this
    }

    lc.execute(state)
  }
}
