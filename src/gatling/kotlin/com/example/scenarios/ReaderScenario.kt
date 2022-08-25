package com.example.scenarios


import com.example.data.*
import com.example.data.stringArrToList
import com.example.extensions.*
import io.gatling.javaapi.core.ChainBuilder
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.Session
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpRequestActionBuilder

class ReaderScenario {

    private fun getRandomUser(): HttpRequestActionBuilder =
        http("Get User").get("/users/${faker.random().nextInt(1, 100)}")
            .headers(defaultHeaders)
            .check(status shouldBe 200)
            .check(jsonId saveAsInt  "userId")

    private fun getTodos(): HttpRequestActionBuilder =
        http("Get Todos").get("/todos")
            .headers(defaultHeaders)
            .queryParam("user", "#{userId}")
            .check(status shouldBe 200)
            .check(jsonAllItems.count.gte(1))

    private fun getPosts(): HttpRequestActionBuilder =
        http("Get Posts").get("/posts")
            .headers(defaultHeaders)
            .check(status shouldBe 200)
            .check(jsonPath("$[*]").count.gte(99)) // jsonPath("$[*]").count().transform(Integer::valueOf).gte(1000)
            .check(jsonPath("$[*].comments").findRandom()
                .transform(::stringArrToList)
                .saveAs("randomComments")
            )

    private fun getComment(): HttpRequestActionBuilder =
        http("Get Comment").get("/comments/#{commentId}")
            .headers(defaultHeaders)
            .check(status shouldBe 200)
            .check(jsonPath("$.id").ofInt shouldBe "#{commentId}") // .jsonPath("$.id").ofInt().isEL("#{commentId}")

    private fun getTodo(id: String): HttpRequestActionBuilder =
        http("Get Todo").get("/todos/$id")
            .check(status.isIn(200, 404))
            .check(jsonPath("$.id").ofInt().isEL(id))


    private fun resetCommentId(session: Session): Session{
        return if (session.getInt("commentId") > 2000){
            session.set("commentId", faker.random().nextInt(1, 2000))
        } else session
    }

    private fun setRandomIdVars(session: Session): Session {
        val randNum = faker.random().nextInt(1, 100)
        return session.setAll(mapOf(
            "commentId" to randNum,
            "idLessThan50" to (randNum < 50)
        ))
    }


    val scn: ChainBuilder =
        call(getRandomUser())
            .call(getTodos())
            .pause(1.sec)
            .call(getPosts())
            .pause(500.ms)
            .forEach("#{randomComments}", "commentId") {
                call(getComment())
                    .exec(::resetCommentId)
                    .call(getTodo("#{commentId}"))
                    .pause(500.ms)
            }
            .pause(1)
            .exec(::setRandomIdVars)
            .asLongAs("#{idLessThan50}") { // .asLongAs({ session -> session.getBoolean("commentIdIsLessThan50") }){ ... }
                call(getComment())
                    .printSessionVar("commentId")
                    .pause(1.sec)
                    .exec(::setRandomIdVars) // reset condition with new random id value
            }


    companion object {
        val scn: ChainBuilder by lazy { ReaderScenario().scn }
    }

}


