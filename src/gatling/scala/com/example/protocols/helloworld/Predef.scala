package com.example.protocols.helloworld

import com.example.protocols.helloworld.protocol.{HelloWorld, HelloWorldProtocol, HelloWorldProtocolBuilder}

import scala.language.implicitConversions

object Predef {

  val helloWorld = HelloWorldProtocolBuilder

  implicit def helloWorldBuilderToProtocol(builder: HelloWorldProtocolBuilder): HelloWorldProtocol = builder.build()


  def helloWorld(funcName: String) = new HelloWorld(funcName)

}
