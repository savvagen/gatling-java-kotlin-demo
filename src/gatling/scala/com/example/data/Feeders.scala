package com.example.data

import io.gatling.core.feeder.FeederBuilderBase
import io.gatling.core.Predef._

object Feeders extends RandomScalaData {

  /*val categoryFeed: Iterator[Unit] = Iterator.continually {
    val cats = List("cats", "dogs", "test")
    Map("randomCategory" -> cats(Random.nextInt(cats.size)))
  }*/

  val categoryFeed: FeederBuilderBase[String] = Array(
    Map("category" -> "cats"),
    Map("category" -> "dogs"),
    Map("category" -> "test")
  ).random

  val randomUserFeeder: Iterator[Map[String, String]] = Iterator.continually {
    Map("randomUserJson" -> randomUser())
  }


}
