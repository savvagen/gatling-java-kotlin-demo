package com.example.protocols.helloworld.protocol

import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.protocol.{Protocol, ProtocolComponents, ProtocolKey}
import io.gatling.core.session.Session
import io.gatling.core.{CoreComponents, protocol}


class HelloWorldProtocol(val baseUrl: String, val defaultMessage: String) extends Protocol {
  type Components = HelloWorldComponents
}

case class HelloWorldComponents(upperProtocol: HelloWorldProtocol) extends ProtocolComponents {
  override def onStart: Session => Session = Session.Identity
  override def onExit: Session => Unit = ProtocolComponents.NoopOnExit
}


object HelloWorldProtocol {
  def apply(baseUrl: String, defaultMessage: String) = new HelloWorldProtocol(baseUrl, defaultMessage)

  val UpperProtocolKey: ProtocolKey[HelloWorldProtocol, HelloWorldComponents] = new ProtocolKey[HelloWorldProtocol, HelloWorldComponents] {
    type Protocol = HelloWorldProtocol
    type Components = HelloWorldComponents

    override def protocolClass: Class[protocol.Protocol] = classOf[HelloWorldProtocol].asInstanceOf[Class[io.gatling.core.protocol.Protocol]]
    override def defaultProtocolValue(configuration: GatlingConfiguration): HelloWorldProtocol = throw new IllegalStateException("Can't provide a default value for UpperProtocol")
    override def newComponents(coreComponents: CoreComponents): HelloWorldProtocol => HelloWorldComponents = {
      playwrightProtocol => HelloWorldComponents(playwrightProtocol)
    }
  }
}


object  HelloWorldProtocolBuilder {
  def withBaseUrl(baseUrl: String): HelloWorldProtocolBuilderMessage = HelloWorldProtocolBuilderMessage(baseUrl)
}

case class  HelloWorldProtocolBuilderMessage(baseUrl: String) {
  def withDefaultMessage(defaultMessage: String) = HelloWorldProtocolBuilder(baseUrl, defaultMessage)
}

case class HelloWorldProtocolBuilder(var baseUrl: String, var defaultMessage: String) {
  def build(): HelloWorldProtocol = HelloWorldProtocol(
    baseUrl = baseUrl,
    defaultMessage = defaultMessage
  )
}