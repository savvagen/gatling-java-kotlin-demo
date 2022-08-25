package com.example.scenarios;

import com.example.data.RandomDataJava;
import io.gatling.javaapi.core.ChainBuilder;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.doIf;
import static io.gatling.javaapi.core.CoreDsl.pause;

public abstract class BaseScnJava extends RandomDataJava {

    public Map<String, String> defaultHeaders = new HashMap<>();

    public BaseScnJava(){
        defaultHeaders.put("Content Type", "application/json; charset=utf8");
        defaultHeaders.put("Accept", "application/json");
    }

    public ChainBuilder stopInjectorIfFailed() {
        return doIf(session -> session.asScala().isFailed()).then(
                pause(Duration.ofSeconds(10)).stopInjector("Found failed http-request. The Injector was stopped after 5 sec.")
        );
    }


}
