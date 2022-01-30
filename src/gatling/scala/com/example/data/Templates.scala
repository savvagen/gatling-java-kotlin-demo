package com.example.data

import com.example.data.RandomData.dateTimeNow
import com.example.models.{Post, User}
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.javafaker.Faker
import com.google.gson.Gson
import io.gatling.core.session.Expression
import io.gatling.commons.validation._

import java.time.OffsetDateTime
import java.util
import java.util.{Date, Locale}

object Templates {

  var faker = new Faker(new Locale("en_US"))
  var gson = new Gson()
  var objectMapper = new ObjectMapper()

  val userTemplate: Expression[String] = session =>
    for {
      token <- session("token").validate[String]
      //foo <- session("foo").validate[String]
      //bar <- session("bar").validate[String]
    } // yield s"""{ "token": "$token" "foo": "$foo", "bar": "$bar" }"""
    yield s"""
             |{
             |    "name": "${faker.name().firstName()} ${faker.name().lastName()}",
             |    "username": "${faker.name().username()}",
             |    "email": "${faker.internet().emailAddress()}",
             |    "createdAt": "${RandomData.dateTimeNow()}"
             |}
             |""".stripMargin

  /*val userTemplateRandom: Expression[String] = session =>
    s"""
       |{
       |    "name": "${faker.name().firstName()} ${faker.name().lastName()}",
       |    "username": "${faker.name().username()}",
       |    "email": "${faker.internet().emailAddress()}",
       |    "createdAt": "${RandomData.dateTimeNow()}"
       |}
       |""".stripMargin.success*/

  val userTemplateRandom: Expression[String] = session =>
    objectMapper.writeValueAsString(new User()
      .setName(s"${faker.name().firstName()} ${faker.name().lastName()}")
      .setUsername(faker.name.username())
      .setEmail(faker.internet.emailAddress())
      .setCreatedAt(RandomData.dateTimeNow())
    ).success

  val postTemplateRandom: Expression[String] = session => {
    def randId: Integer = faker.random().nextInt(100, 999)
    objectMapper.writeValueAsString(new Post()
      .setTitle(s"Test Post from ${OffsetDateTime.now()} ${faker.random().nextInt(100000, 999999)}")
      .setSubject("Performance Testing")
      .setBody(faker.lorem().sentence())
      .setUser(Integer.valueOf(session("userId").as[String]))
      .setCreatedAt(dateTimeNow())
      .setComments(util.Arrays.asList(randId, randId)) //.setComments(util.List.of(faker.random().nextInt(100, 999)))
    ).success
  }

}
