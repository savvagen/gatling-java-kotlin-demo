package com.example.cases
import io.gatling.commons.stats.KO
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt


object ErrorCases {

  def stopInjectorIfFailed: ChainBuilder = {
    doIf((session: Session) => session.status == KO){
      pause(10.seconds).stopInjector("Stopping Injector after 10 seconds. Cause: failed request")
    }
  }

}
