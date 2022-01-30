package com.example.scenarios

import com.example.data.RandomData
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

import java.util.Date

class BaseScenario extends RandomData {

  def setUpScn(username: String, password: String): ChainBuilder = {
    exec(_.set("username", username))
      .exec(_.set("password", password))
      .exec(_.set("currentDate", new Date()))
  }


}
