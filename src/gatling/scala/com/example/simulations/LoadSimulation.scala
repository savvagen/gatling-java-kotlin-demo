package com.example.simulations

import com.example.data.RandomData
import com.example.protocols.HttpSimulation
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import com.example.scenarios.{PostReaderScenario, PostWriterScenario}

class LoadSimulation extends HttpSimulation {


  val writerScn: ScenarioBuilder = scenario("Post Writer Scenario")
    .exec(PostWriterScenario.scn("test", "test", 2 to 5, 3))

  val readerScn: ScenarioBuilder = scenario("Post Reader Scenario")
    .exec(PostReaderScenario.scn("test", "test"))

  setUp(
    writerScn.inject(atOnceUsers(2))
  ).protocols(httpConf)

}
