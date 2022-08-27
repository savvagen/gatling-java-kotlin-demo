package com.example.protocols.helloworld.protocol


import io.gatling.commons.stats.{KO, OK}
import io.gatling.commons.util.Clock
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.action.{Action, ExitableAction}
import io.gatling.core.session.Session
import io.gatling.core.stats.StatsEngine
import io.gatling.core.structure.ScenarioContext
import io.gatling.core.util.NameGen

class HelloWorldMessageActionBuilder(funcName: String, message: String) extends ActionBuilder {

  override def build(ctx: ScenarioContext, next: Action): Action = {
    import ctx._
    val statsEngine = coreComponents.statsEngine
    val upperComponents = protocolComponentsRegistry.components(HelloWorldProtocol.UpperProtocolKey)
    new HelloWorldMessageAction(funcName, message, upperComponents.upperProtocol, statsEngine, coreComponents.clock, next)
  }

}


class HelloWorldMessageAction(funcName: String, message: String, protocol: HelloWorldProtocol, val statsEngine: StatsEngine, val clock: Clock, val next: Action) extends ExitableAction with NameGen {
  override def name: String = funcName

  override def execute(session: Session): Unit = {
    val startTime = clock.nowMillis
    try {
      val message = if (this.message.nonEmpty) this.message else protocol.defaultMessage
      println(s"Message: ${message} to url ${protocol.baseUrl}")
      session.set("helloWorldMessage", message)
      statsEngine.logResponse(session.scenario, session.groups, name, startTime, clock.nowMillis, OK, None, None)
    } catch {
      case e: Throwable =>
        logger.error(e.getMessage, e)
        statsEngine.logResponse(session.scenario, session.groups, name, startTime, clock.nowMillis, KO, Some(s"Could not print the message $message..."), Some(e.getMessage))
    }
    next.!(session) // next ! session
  }
}