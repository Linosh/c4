package com.c4.core.lifecycle

import org.scalatest._

/**
  * Created by dmitriiiermiichuk on 6/13/16.
  */
class LifecycleSpec extends FlatSpec {
  "A lifecycle" should "have execute method" in {
    val lc = new LifecyclePhase[String, MapLikeState[String, String]] {
      override val name: String = "Test"

      override def execute(state: MapLikeState[String, String]): Unit = {
        println("Hello World")
      }

      override val next = None
      override val previous = None
    }

    lc.execute(MapLikeState[String, String]())
  }
}
