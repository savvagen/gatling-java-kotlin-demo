package com.example


import com.example.simulations.KotlinLoadSimulation
import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

fun main() {
    val props = GatlingPropertiesBuilder()
    props.simulationClass(KotlinLoadSimulation::class.java.name)

    Gatling.fromMap(props.build())
}