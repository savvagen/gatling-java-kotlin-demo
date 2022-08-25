package com.example.data
import io.gatling.core.session.Expression
import io.gatling.commons.validation._


object Templates extends RandomScalaData {

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
             |    "createdAt": "${RandomDataJava.dateTimeNow()}"
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
    randomUser().success

  val postTemplateRandom: Expression[String] = session => {
    def randId: Integer = faker.random().nextInt(1, 100)
    randomPost(randId).success
  }

}
