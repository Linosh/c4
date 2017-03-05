package com.mmdd.it

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.mmdd.api.AkkaHttpApi
import org.scalatest.{FlatSpec, Matchers}

class PerformanceTest extends FlatSpec with Matchers with ScalatestRouteTest {
  behavior of "mmdd"

  it should "handle power of the one" in {
    Get("/hahaha") ~> AkkaHttpApi.routes ~> check {
      handled shouldBe true
    }
  }
}
