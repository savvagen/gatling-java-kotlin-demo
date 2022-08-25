package com.example.scenarios

import com.example.cases.ErrorHandling.stopInjectorIfFailed
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.ChainBuilder
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object PostWriterScenario extends BaseScenario {

  var authHeaders = Map(
    "Authorization" -> "Bearer #{token}",
    "Content-Type" -> "application/json"
  )

  def getToken: ChainBuilder = {
    exec(http("GET /get_token").get("/get_token")
      .basicAuth("#{username}", "#{password}")
      .check(status.is(200))
      .check(jsonPath("$.token").saveAs("token")))
  }

  def createUser(): ChainBuilder = {
    //feed(randomUserFeeder)
    exec(http("POST /users").post("/users")
      .headers(authHeaders)
      .body(StringBody(session =>
        s"""
           |{
           |    "name": "${faker.name().firstName()} ${faker.name().lastName()}",
           |    "username": "${faker.name().username()}",
           |    "email": "${faker.internet().emailAddress()}",
           |    "createdAt": "#{currentDate(yyyy-MM-dd'T'HH:mm:ss.SSS'Z')}"
           |}
           |""".stripMargin)).asJson
      //.body(StringBody(Templates.userTemplateRandom)).asJson
      //.body(StringBody(session => randomUser().success)).asJson
      //.body(StringBody(session => session("randomUserJson").as[String])).asJson // Get Json from feeder
      .check(status.is(201))
      .check(jsonPath("$.id").ofType[Int].saveAs("userId"))
      .check(jsonPath("$.email").saveAs("userEmail"))
    )
  }

  def createPost(): ChainBuilder = {
    exec(http("POST /posts").post("/posts")
      .header("Content Type", "application/json")
      .body(StringBody(session => randomPost(session("userId").as[Int]))).asJson
      .check(status.is(201))
      .check(jsonPath("$.id").saveAs("postId"))
    )
  }

  def createComment(): ChainBuilder = {
    exec(http("POST /comments").post("/comments")
      .header("Content Type", "application/json")
      .body(StringBody(session =>
        randomComment(session("postId").as[Int], session("userEmail").as[String]))
      ).asJson
      .check(status.is(201))
      .check(jsonPath("$.post").ofType[String].is("#{postId}"))
      .check(jsonPath("$.email").is("#{userEmail}"))
    )
  }


  def scn(username: String, password: String, postsNumber: Range = 2 to 5, commentsNumber: Int = 3): ChainBuilder = {
    exec(setUpScn(username, password))
      .exec(getToken).exec(stopInjectorIfFailed)
      .pause(500 milliseconds)
      .exec(createUser()).exec(stopInjectorIfFailed)
      .pause(1.second)
      // Create new Posts and Comments
      .repeat(1){
        exec(createPost()).exec(stopInjectorIfFailed)
      }
      .exec(_.set("times", postsNumber))
      .repeat("${times.random()}", "postIndex"){
        exec(createPost()).exec(stopInjectorIfFailed)
          .pause(1.second)
          .repeat(commentsNumber, "commentIndex"){
            exec(createComment()).exec(stopInjectorIfFailed)
              .pause(1)
          }
      }
  }




}
