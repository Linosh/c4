package com.c4.core.lifecycle

import org.scalatest._

/**
  * Created by dmitriiiermiichuk on 7/3/16.
  */
class LifecycleTest extends FlatSpec {
  behavior of "Lifecycle"

  it should "have a name" in {
    val lc = Lifecycle.seqLifecycleBuilder[MapLikeState[String, String]].withName("LF").build()

    assertResult("LF")(lc.name)
  }

  it should "be able to contains phases" in {
    val lcf = LifecyclePhase[MapLikeState[String, String]]("Phase 1", state => state.add("k", "v"))

    val lc = Lifecycle.seqLifecycleBuilder[MapLikeState[String, String]].withName("LF").withPhase(lcf).build()

    assertResult(lcf)(lc.phases.head)
  }
}
