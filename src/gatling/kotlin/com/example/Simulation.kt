package com.example

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status
import java.time.Duration


class KotlinSimulation : Simulation() {

    var httpConf = http.baseUrl("http://localhost:3001")
        .contentTypeHeader("application/json")
        .acceptCharsetHeader("application/json")
        .disableWarmUp()
        .disableCaching()

    var scn = scenario("Get Users")
        .exec(http("Get gatling").get("/users").check(status().`is`(200)))
        .pause(Duration.ofSeconds(1))
        .exec(http("Get Posts").get("/posts").check(status().`is`(200)))

    init {
        setUp(scn.injectOpen(
            atOnceUsers(1),
            nothingFor(4),
            constantUsersPerSec(1.0).during(10),
        )).protocols(httpConf)
    }
}


fun main() {
    val props = GatlingPropertiesBuilder()
    props.simulationClass(KotlinSimulation::class.java.name)

    Gatling.fromMap(props.build())
}