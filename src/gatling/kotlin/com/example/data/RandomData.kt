package com.example.data

import com.example.extensions.toJson
import com.example.modelds.Comment
import com.example.modelds.Post
import com.example.modelds.User
import com.github.javafaker.Faker
import com.google.gson.Gson
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

val gson = Gson()
val faker = Faker(Locale.ENGLISH)
fun dateTimeNow(): String = OffsetDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

val defaultHeaders = mapOf(
    "Content Type" to "application/json; charset=utf8",
    "Accept" to "application/json"
)

fun jsonStr(obj: Any): String = gson.toJson(obj)

/*
Transform array string "[1, 2, 3, 4, 5]" to List<Int>"
 */
fun stringArrToList(arrayString: String): List<Int> = arrayString
    .replace("\\s+","")
    .substring(1, arrayString.length - 1).split(",")
    .map(Integer::valueOf)


fun randomCategory(): String = listOf("cats", "dogs", "test")[faker.random().nextInt(0, 2)]

fun fakeUser(): String =
    User(
        name = "${faker.name().firstName()} ${faker.name().lastName()}",
        username = faker.name().username(),
        email = faker.internet().emailAddress(),
        createdAt = dateTimeNow()
    ).toJson



fun fakePost(userId: Int): String =
    Post(
        title = "My Fake post from ${dateTimeNow()}",
        subject = "Performance Testing",
        body = faker.lorem().sentence(),
        category = randomCategory(),
        user = userId,
        createdAt = dateTimeNow(),
        comments = mutableListOf(faker.random().nextInt(50, 100), faker.random().nextInt(1, 49))
    ).toJson

fun fakeComment(post: Int, email: String): String =
    Comment(
        name = "Positive Comment",
        body = "Hello There",
        likes = mutableListOf(1),
        dislikes = mutableListOf(0),
        email = email,
        post = post,
        createdAt = dateTimeNow(),
    ).toJson

class RandomData {

    companion object {
        val fakeUser: String by lazy {
            User(
                name = "${faker.name().firstName()} ${faker.name().lastName()}",
                username = faker.name().username(),
                email = faker.internet().emailAddress(),
                createdAt = dateTimeNow()
            ).toJson
        }
    }

}
