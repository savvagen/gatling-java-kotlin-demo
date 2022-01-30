package com.example.protocols

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder


class HttpSimulation extends Simulation {

  def getProperty(propertyName: String, defaultValue: String): String = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }

  def baseUrl: String = getProperty("BASE_URL", "http://localhost:3001")

  var httpConf: HttpProtocolBuilder = http.baseUrl(baseUrl)
    .userAgentHeader("")
    .acceptHeader("application/json")
    .acceptCharsetHeader("utf-8")
    .contentTypeHeader("application/json; charset=utf8")
    //.proxy(Proxy("localhost", 8888).httpsPort(8888)) // Send All requests through proxy (Fiddler or Charles, etc...)
    .disableWarmUp
    .disableCaching



}
