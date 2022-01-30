package com.example.cases
import com.example.data.RandomData
import io.gatling.commons.stats.KO
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt


object ErrorHandling extends RandomData {

  def exitOnFailure: Boolean = getProperty("STOP_TEST_ON_FAILURE", "false").toBoolean

  def stopInjectorIfFailed: ChainBuilder = {

    doIf((session: Session) => (session.status == KO && exitOnFailure)){
      pause(10.seconds).stopInjector("Stopping Injector after 10 seconds. Cause: failed request")
    }
  }

}
