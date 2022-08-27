package com.example.simulations

import com.example.protocols.helloworld.protocol.{HelloWorldProtocol, HelloWorldProtocolBuilder}
import io.gatling.core.Predef.{Simulation, scenario}
import com.example.protocols.helloworld.Predef._
import io.gatling.core.Predef._

import scala.concurrent.duration.DurationInt



class HelloWorldSimulation extends Simulation {

  val helloWorldProtocol: HelloWorldProtocol = helloWorld
    .withBaseUrl("http://hello_world")
    .withDefaultMessage("Hello World")

  val scn = scenario("Hello World Scn")
    .exec(helloWorld("HelloWorld1").sendMessage("Hello World_1"))
    .pause(1.second)
    .exec(helloWorld("HelloWorld2").sendMessage("Hello World_2"))

  setUp(scn.inject(
    constantConcurrentUsers(1).during(30.seconds)
  )).protocols(helloWorldProtocol)

}
