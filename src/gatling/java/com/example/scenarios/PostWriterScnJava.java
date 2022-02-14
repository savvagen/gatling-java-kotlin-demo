package com.example.scenarios;

import com.example.data.JavaTemplates;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.gatling.javaapi.core.ChainBuilder;
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
        return exec(http("GET /get_token").get("/get_token")
                .basicAuth("test", "test")
                .header("Content Type", "application/json")
                .check(List.of(
                    status().is(200),
                    jsonPath("$.token").saveAs("token")
                )));
    }

    public ChainBuilder createUser() {
        return exec(http("POST /users").post("/users")
                .header("Authorization", "Bearer #{token}")
                .headers(defaultHeaders)
                .body(StringBody(JavaTemplates.userTemplate)).asJson()
                /*.body(StringBody(session -> gson.toJson(new User().setName(faker.name().firstName() + " " + faker.name().lastName())
                        .setUsername(faker.name().username())
                        .setEmail(faker.internet().emailAddress())
                        .setCreatedAt("#{currentDate(<yyyy-MM-dd'T'HH:mm:ss.SSS'Z'>)}")
                ))).asJson()*/
                .check(status().is(201))
                .check(jsonPath("$.id").saveAs("userId"))
        );
    }

    public ChainBuilder createPost(){
        return exec(http("POST /posts").post("/posts")
                .headers(defaultHeaders)
                .body(StringBody(session -> randomPost(session.getString("userEmail"))))
                .check(status().is(201))
                .check(jsonPath("$.id").saveAs("postId"))
        );
    }





    public ChainBuilder getUsers() {
        return exec(http("Get Users").get("/users")
                .header("Content Type", "application/json")
                .check(status().is(200))
        );
    }

    public ChainBuilder getPosts() {
        return exec(http("Get Posts").get("/posts")
                .header("Content Type", "application/json")
                .check(status().is(200))
        );
    }

    public ChainBuilder scn =
            exec(session -> session.set("currentDate", new java.util.Date()))
                    .exec(getToken()).exec(stopInjectorIfFailed())
                    .pause(Duration.ofMillis(500))
                    .exec(createUser()).exec(stopInjectorIfFailed())
                    .pause(Duration.ofSeconds(1))


                    .exec(getUsers())
                    .pause(Duration.ofSeconds(1))
                    .exec(getPosts())
                    .exec(session -> {
                        System.out.println("Found User ID: " + session.getString("userId"));
                        return session;
                    });


}
