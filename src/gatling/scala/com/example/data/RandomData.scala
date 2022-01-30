package com.example.data

import com.example.models.{Comment, Post, User}
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.javafaker.Faker
import com.google.gson.Gson
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util
import java.util.Locale

class RandomData {

  def getProperty(propertyName: String, defaultValue: String): String = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }

  val faker = new Faker(new Locale("en_US"))
  val objectMapper = new ObjectMapper()
  val gson = new Gson()
  val isoDateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  val simpleDateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  def dateTimeNow(): String = OffsetDateTime.now.format(isoDateFormat)

  def randomUser(): String = {
    objectMapper.writeValueAsString(new User()
      .setName(s"${faker.name().firstName()} ${faker.name().lastName()}")
      .setUsername(faker.name.username())
      .setEmail(faker.internet.emailAddress())
      .setCreatedAt(dateTimeNow()))
  }

  def randomPost(userId: Int): String = {
    def randomCategory(): String = {
      val cats = List("cats", "dogs", "test")
      cats(faker.random().nextInt(0, (cats.size - 1)))
    }
    def randId: Integer = faker.random().nextInt(100, 999)
    objectMapper.writeValueAsString(new Post()
      .setTitle(s"Test Post from ${OffsetDateTime.now()} ${faker.random().nextInt(100000, 999999)}")
      .setSubject("Performance Testing")
      .setBody(faker.lorem().sentence())
      .setUser(Integer.valueOf(userId))
      .setCreatedAt(dateTimeNow())
      .setCategory(randomCategory())
      .setComments(util.Arrays.asList(randId, randId)) //.setComments(util.List.of(faker.random().nextInt(100, 999)))
    )
  }

  def randomComment(postId: Int, email: String): String = {
    objectMapper.writeValueAsString(new Comment()
      .setPost(postId)
      .setEmail(email)
      .setName(s"My Comment ${faker.random().nextInt(100000, 999999)}")
      .setLikes(util.Arrays.asList(0))
      .setDislikes(util.Arrays.asList(0))
      .setCreatedAt(dateTimeNow())
      .setBody("Hello World"))
  }

}

object RandomData extends RandomData {


  override def dateTimeNow(): String = super.dateTimeNow()


}