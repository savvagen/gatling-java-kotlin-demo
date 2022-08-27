package com.example

import com.example.simulations.{HelloWorldSimulation, ScalaLoadSimulation}
import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object Runner {

  def main(args: Array[String]): Unit = {

    val simulationClass = classOf[ScalaLoadSimulation].getName

    val props = new GatlingPropertiesBuilder
    props.simulationClass(simulationClass)

    Gatling.fromMap(props.build)

  }

}
