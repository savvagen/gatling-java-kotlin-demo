package com.example.scenarios

import com.example.cases.ErrorHandling.stopInjectorIfFailed
import com.example.data.Feeders.categoryFeed
import com.example.data.RandomScalaData
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._

import java.util.Date
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object PostReaderScenario extends BaseScenario {

  val defaultHeaders = Map(
    "Content Type" -> "application/json; charset=utf8"
  )

  def getPosts: ChainBuilder = {
    feed(categoryFeed)
    .exec(http("GET /posts").get("/posts")
      .headers(defaultHeaders)
      .queryParam("category", "#{category}")
      .check(status is 200)
      .check(jsonPath("$[*]").count.gte(10))
      .check(jsonPath("$[*].id").findRandom(6).saveAs("postIds"))
      .check(jsonPath("$[*].id").findRandom(6).transform(seq => seq.mkString(",")).saveAs("postIdsComasep"))
    )
  }

  def getPost(postId: String): ChainBuilder = {
    exec(http("GET /posts/ID").get(s"/posts/$postId")
      .headers(defaultHeaders)
      .check(status.is(200))
      .check(jsonPath("$.id").notNull)
      .check(jsonPath("$.comments[*]").findAll.saveAs("commentIds"))
    )
  }

  def getComment(commentId: String): ChainBuilder = {
    exec(http("GET /comments/ID").get(s"/comments/$commentId")
      .headers(defaultHeaders)
      .check(status.is(200))
      .check(jsonPath("$.id").notNull)
    )
  }


  def scn(username: String, password: String): ChainBuilder = {
    exec(setUpScn(username, password))
      // Open Posts Page
      .exec(getPosts).exec(stopInjectorIfFailed)
      .pause(200.millis)
      .foreach("#{postIds}", "postId"){
        exec(getPost("#{postId}")).exec(stopInjectorIfFailed)
      }
      // Open random post
      .pause(2.seconds)
      .exec(getPost("#{postIds.random()}")).exec(stopInjectorIfFailed)
      .pause(10.millis)
      .foreach("#{commentIds}", "commentId"){
          exec(getComment("#{commentId}")).exec(stopInjectorIfFailed)
      }
  }


}
