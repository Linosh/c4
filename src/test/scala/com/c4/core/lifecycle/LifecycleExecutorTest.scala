package com.c4.core.lifecycle

import org.scalatest._

/**
  * Created by dmitriiiermiichuk on 7/3/16.
  */
class LifecycleExecutorTest extends FlatSpec {
  behavior of "LifecycleExecutor"

  it should "be able to execute sequence of phases" in  {
    type STATE = MapLikeState[String, String]

    val lf = Lifecycle.seqLifecycleBuilder[STATE]
      .withName("Simple LF")
      .withPhase(LifecyclePhase[MapLikeState[String, String]]("Phase 1", state => state.add("k1", "v1")))
      .withPhase(LifecyclePhase[MapLikeState[String, String]]("Phase 1", state => state.add("k2", "v2")))
      .build()

    val state = LifecycleExecutor.seqExecutor[STATE].execute(lf, MapLikeState())

    assertResult("v1")(state.get("k1").get)
    assertResult("v2")(state.get("k2").get)
  }
}
