package com.example

import com.example.simulations.LoadSimulation
import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object Runner {

  def main(args: Array[String]): Unit = {

    val simulationClass = classOf[LoadSimulation].getName

    val props = new GatlingPropertiesBuilder
    props.simulationClass(simulationClass)

    Gatling.fromMap(props.build)

  }

}
