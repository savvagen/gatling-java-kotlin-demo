package com.example.scenarios

import com.example.data.RandomScalaData
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

import java.util.Date

abstract class BaseScenario extends RandomScalaData {

  def setUpScn(username: String, password: String): ChainBuilder = {
    exec(_.set("username", username))
      .exec(_.set("password", password))
      .exec(_.set("currentDate", new Date()))
  }

}
