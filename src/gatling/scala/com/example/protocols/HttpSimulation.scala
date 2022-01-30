package com.example.protocols

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder


class HttpSimulation extends Simulation {


  var httpConf: HttpProtocolBuilder = http.baseUrl("http://localhost:3001")
    .userAgentHeader("")
    .acceptHeader("application/json")
    .acceptCharsetHeader("utf-8")
    .contentTypeHeader("application/json; charset=utf8")
    //.proxy(Proxy("localhost", 8888).httpsPort(8888)) // Send All requests through proxy (Fiddler or Charles, etc...)
    .disableWarmUp
    .disableCaching



}
