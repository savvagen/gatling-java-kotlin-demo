package com.example.scenarios;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Session;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class PostReaderScnJava extends BaseScnJava {

    public ChainBuilder stopInjectorIfFailed() {
        return doIf(session -> session.asScala().isFailed()).then(
                pause(Duration.ofSeconds(10)).stopInjector("Stopping Injector after 10 seconds. Cause: failed request")
        );
    }

    public ChainBuilder getRandomUser() {
        return exec(http("Get Users").get("/users/" + faker.random().nextInt(1,100))
                .header("Content Type", "application/json")
                .check(status().is(200))
                .check(jsonPath("$.id").saveAs("userId"))
        );
    }

    public ChainBuilder getTodos(){
        return exec(http("Get Todos").get("/todos")
                .queryParam("user", "#{userId}")
                .check(status().is(200))
        );
    }

    public ChainBuilder getPosts() {
        return exec(http("Get Posts").get("/posts")
                .header("Content Type", "application/json")
                .check(status().is(200))
                //.check(jsonPath("$[*].comments").ofList().findRandom().saveAs("randomComments"))
                .check(jsonPath("$[*].comments")
                        .findRandom()
                        .transform(s -> Arrays.stream(s.substring(1, s.length()-1).split(","))
                                .map(Integer::valueOf).collect(Collectors.toList())) // transform string: "[1,2,4]" to List<Integer>
                        .saveAs("randomComments"))

        );
    }

    public ChainBuilder getComment(){
        return exec(http("Get Comment").get("/comments/#{commentId}")
                .header("Content Type", "application/json")
                .check(status().is(200))
                .check(jsonPath("$.id").ofInt().isEL("#{commentId}"))
        );
    }

    public ChainBuilder getTodo(String id){
        return exec(http("Get Todo").get("/todos/" + id)
                .check(status().in(200, 404))
                .check(jsonPath("$.id").ofInt().isEL(id)) // check session variable from parameter
        );
    }


    /**
     * Set variables for Conditional loop: asLongAs("#{commentIdIsLessThan50}")
     * @param session
     * @return io.gatling.javaapi.core.Session
     */
    public Session setRandomIdVars(Session session){
        // the same as
        var randNum = faker.random().nextInt(1, 100);
        return session.setAll(new HashMap<>(){{
            put("commentId", randNum);
            put("commentIdIsLessThan50", randNum < 50);
        }});

    }

    public ChainBuilder scn() {
            return exec(getRandomUser()).exec(stopInjectorIfFailed())
                .exec(getTodos()).exec(stopInjectorIfFailed())
                .pause(Duration.ofSeconds(2))
                .exec(getPosts()).exec(stopInjectorIfFailed())
                .pause(Duration.ofMillis(500))
                // Iterate through the list of ids
                .foreach("#{randomComments}", "commentId").on(
                        exec(getComment()).exec(stopInjectorIfFailed())
                                .exec(getTodo("#{commentId}")).exec(stopInjectorIfFailed())
                                .pause(Duration.ofMillis(500))
                )
                .pause(1)
                // Looping till random id is < than 50
                .exec(this::setRandomIdVars)
                .asLongAs("#{commentIdIsLessThan50}").on(
                        exec(getComment()).exec(stopInjectorIfFailed())
                                .pause(1)
                                // reset condition with new random id value
                                .exec(this::setRandomIdVars)
                );
    }
}
