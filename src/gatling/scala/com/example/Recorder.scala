package com.example

import io.gatling.recorder.GatlingRecorder
import io.gatling.recorder.config.RecorderPropertiesBuilder
import java.nio.file.Path

object Recorder extends App {

  val props = new RecorderPropertiesBuilder()
      .resourcesFolder("src/gatling/resources")
      .simulationsFolder("src/gatling/scala/com/example/simulations")
      .simulationPackage("simulations")
      .build

  def option = {
    Option apply(Path.of("src/gatling/resources/gatling.conf"))
  }

  GatlingRecorder.fromMap(props, option)

}
