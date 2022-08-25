package com.example.simulations

import com.example.protocols.HttpSimulation
import io.gatling.core.Predef._

import scala.language.postfixOps
import com.example.scenarios.{PostReaderScenario, PostWriterScenario}
import com.typesafe.config.{Config, ConfigFactory}
import io.gatling.core.controller.inject.closed.ClosedInjectionStep
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class ScalaLoadSimulation extends HttpSimulation {


  def username: String = getProperty("USERNAME", "test")
  def password: String = getProperty("PASSWORD", "test")

  val configName: String = getProperty("CONFIG", "configs/load")
  val config: Config = ConfigFactory.load(configName).withFallback(ConfigFactory.load("config/load"))
  // Load Model
  val rampToUsers: Int = config.getInt("performance.rampToUsers")
  val rampUpTime: Int = config.getInt("performance.rampUpTime")
  val duration: Int = config.getInt("performance.duration")
  val maxDuration: Int = config.getInt("performance.maxDuration")
  val scenarioPostNumber: Int = config.getInt("performance.scenario.postsNumber")
  val scenarioCommentsNumber: Int = config.getInt("performance.scenario.commentsNumber")
  // Assertions
  val responseTimeMaxLte: Int           = config.getInt("performance.global.assertions.responseTime.max.lte")
  val failedRequestsPercentLte: Int     = config.getInt("performance.global.assertions.failedRequests.percent.lte")
  val successfulRequestsPercentGte: Int = config.getInt("performance.global.assertions.successfulRequests.percent.gte")


  val writerScn: ScenarioBuilder = scenario("Writer Scenario")
    .exec(PostWriterScenario.scn(username, password, 2 to 4, 2))

  val readerScn: ScenarioBuilder = scenario("Reader Scenario")
    .exec(PostReaderScenario.scn(username, password))


  def loadModelSteps50Prc(): Seq[ClosedInjectionStep] = {
    Seq(
      rampConcurrentUsers(1).to(rampToUsers/2) during(rampUpTime.seconds),
      constantConcurrentUsers(rampToUsers/2).during(duration.seconds)
    )
  }

  def loadModelSteps100Prc(): Seq[ClosedInjectionStep] = {
    Seq(
      rampConcurrentUsers(1).to(rampToUsers) during(rampUpTime.seconds),
      constantConcurrentUsers(rampToUsers).during(duration.seconds)
    )
  }

  def loadProfile(): List[PopulationBuilder] = {
    List(
      writerScn.inject(loadModelSteps50Prc()),
      readerScn.inject(loadModelSteps50Prc())
    )
  }

  setUp(
    //readerScn.inject(atOnceUsers(1))
    loadProfile()
  ).assertions(
    global.responseTime.max.lte(responseTimeMaxLte),
    global.failedRequests.count.lte(failedRequestsPercentLte),
    global.successfulRequests.percent.gte(successfulRequestsPercentGte)
  ).protocols(httpConf)

}
