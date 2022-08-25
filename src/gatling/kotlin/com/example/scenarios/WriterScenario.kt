package com.example.scenarios

import com.example.data.*
import com.example.extensions.*
import com.example.modelds.Post
import com.example.modelds.User
import io.gatling.javaapi.core.ChainBuilder
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpRequestActionBuilder
import kotlin.jvm.internal.Intrinsics.Kotlin

class WriterScenario {


    private fun getToken(): HttpRequestActionBuilder =
        http("Get Token").get("/get_token")
            .basicAuth("test", "test")
            .check(status shouldBe 200) // .check(status.shouldBe(200))
            .check(jsonPath("$.token") saveAs "token") // .check(jsonPath("$.token").saveAs("token"))

    private fun createUser(): HttpRequestActionBuilder =
        http("Create User").post("/users")
            .header("Authorization", "Bearer #{token}")
            .headers(defaultHeaders)
            .bodyOf { fakeUser() }
            //.body(StringBody(KotlinTemplates.userTemplate))
            //.bodyOf(KotlinTemplates.userTemplate)
            .check(status shouldBe 201)
            .check(jsonId saveAsInt "userId" )
            .check(jsonPath("$.email") saveAs "userEmail")

    private fun createPost(): HttpRequestActionBuilder =
        http("Create Post").post("/posts")
            .headers(defaultHeaders)
            .bodyOf{ s -> fakePost(s.getInt("userId")) }
            .check(status shouldBe 201)
            .check(jsonId.exists())
            .check(jsonId saveAsInt "postId")
            .check(jsonPath("$.user").ofInt shouldBe "#{userId}")
            .check(bodyString().transform{ s -> s.toObj(Post::class.java) }.saveAs("postObj"))

    private fun createComment(): HttpRequestActionBuilder =
        http("Create Comment").post("/comments")
            .headers(defaultHeaders)
            .bodyOf { s -> fakeComment(
                post = s.getInt("postId"),
                email = s.getString("userEmail")
            )}
            .check(status shouldBe 201)
            .check(jsonId.exists())
            .check(jsonId saveAsInt "commentId" )
            .check(jsonPath("$.email").isEL("#{userEmail}"))


    private fun updatePost(): HttpRequestActionBuilder =
        http("Add Post Comments").put("/posts/#{postId}")
            .headers(defaultHeaders)
            .bodyOf { s -> (s.get("postObj") as Post)
                .addComments(s.getInt("commentId")).toJson
            }
            .check(status shouldBe 200)
            .check(jsonId.ofInt.isEL("#{postId}"))
            .check(jsonPath("$.user").ofInt.isEL("#{userId}"))
            .check(bodyString().saveAs("post"))



    fun scn(postsNumber: Int): ChainBuilder =
        call(getToken())
            .pause(1.sec)
            .call(createUser())
            .pause(1.sec)
            .printSessionVar("userId")
            .repeat(postsNumber){
                call(createPost())
                    .printSessionVar("postId")
                    .printSessionVar("postObj")
                    .pause(1.sec)
                    .exec{s -> s.set("times", faker.random().nextInt(3,4))}
                    .repeat("#{times}"){
                        call(createComment())
                            //.call(updatePost())
                            .pause(1.sec)
                    }
            }

    companion object {
        val scn: ChainBuilder by lazy { WriterScenario().scn(2) }
        fun scn(postsNumber: Int): ChainBuilder = WriterScenario().scn(postsNumber)
    }
}