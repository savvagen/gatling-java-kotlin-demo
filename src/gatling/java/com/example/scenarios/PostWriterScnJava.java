package com.example.scenarios;

import com.example.data.JavaTemplates;
import com.example.javaModels.Comment;
import com.example.javaModels.Post;
import com.example.javaModels.User;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.gatling.javaapi.core.ChainBuilder;
import net.sf.saxon.om.Chain;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import java.time.Duration;
import java.util.*;

public class PostWriterScnJava extends BaseScnJava {


    public ChainBuilder stopInjectorIfFailed() {
        return doIf(session -> session.asScala().isFailed()).then(
                pause(Duration.ofSeconds(10)).stopInjector("Stopping Injector after 10 seconds. Cause: failed request")
        );
    }

    public ChainBuilder getToken() {
        return exec(http("Get Token").get("/get_token")
                .basicAuth("test", "test")
                .header("Content Type", "application/json")
                .check(List.of(
                    status().is(200),
                    jsonPath("$.token").saveAs("token")
                )));
    }

    public ChainBuilder createUser() {
        return exec(http("Create User").post("/users")
                .header("Authorization", "Bearer #{token}")
                .headers(defaultHeaders)
                .body(StringBody(JavaTemplates.userTemplate)).asJson()
                /*.body(StringBody(session -> gson.toJson(new User()
                        .setName(faker.name().firstName() + " " + faker.name().lastName())
                        .setUsername(faker.name().username())
                        .setEmail(faker.internet().emailAddress())
                        .setCreatedAt(session.getString("currentDate"))
                ))).asJson()*/
                .check(status().is(201))
                .check(jsonPath("$.id").saveAs("userId"))
                .check(jsonPath("$.email").saveAs("userEmail"))
        );
    }

    public ChainBuilder createPost(){
        return exec(http("Create Post").post("/posts")
                .headers(defaultHeaders)
                .body(StringBody(session -> randomPost(session.getString("userId"))))
                //.body(StringBody(JavaTemplates.postTemplate)).asJson()
                .check(status().is(201))
                .check(bodyString().saveAs("post"))
                .check(jsonPath("$.id").saveAs("postId"))
        );
    }

    public ChainBuilder createComment(){
        return exec(http("Create Comment").post("/comments")
                .headers(defaultHeaders)
                .body(StringBody(session -> gson.toJson(new Comment()
                        .setName("Positive Comment")
                        .setBody("Hello There")
                        .setDislikes(List.of(0))
                        .setLikes(List.of(1))
                        .setPost(session.getInt("postId"))
                        .setEmail(session.getString("userEmail"))
                        .setCreatedAt(dateTimeNow())
                ))).asJson()
                .check(status().is(201))
                .check(jsonPath("$.id").saveAs("commentId"))
        );
    }

    public ChainBuilder updatePost(){
        return exec(http("Add Post Comments").put("/posts/#{postId}")
                .headers(defaultHeaders)
                .body(StringBody(session -> gson.toJson(
                        gson.fromJson(session.getString("post"), Post.class)
                                .addComments(session.getInt("commentId")))
                ))
                .check(status().is(200))
                .check(jsonPath("$.id").isEL("#{postId}"))
                .check(jsonPath("$.user").ofInt().is(session -> session.getInt("userId")))
                .check(bodyString().saveAs("post"))
        );
    }

    public ChainBuilder scn(int postsToCreate) {
       return exec(session -> session.set("currentDate", dateTimeNow()))
                .exec(getToken()).exec(stopInjectorIfFailed())
                .pause(Duration.ofMillis(500))
                .exec(createUser()).exec(stopInjectorIfFailed())
                .pause(1)
                .repeat(postsToCreate).on(
                        exec(createPost()).exec(stopInjectorIfFailed())
                         .pause(1)
                         .exec(session -> session.set("times", faker.random().nextInt(2, 3)))
                         .repeat("#{times}").on(
                                    exec(createComment()).exec(stopInjectorIfFailed())
                                            .exec(updatePost()).exec(stopInjectorIfFailed())
                                            .pause(1)
                                )
               );
    }



}
