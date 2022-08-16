package com.example.simulations;

// required for Gatling core structure DSL
import com.example.scenarios.PostReaderScnJava;
import com.example.scenarios.PostWriterScnJava;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.gatling.javaapi.core.*;

import java.util.List;

import static com.example.protocols.HttpProtocol.*;
import static io.gatling.javaapi.core.CoreDsl.*;
// required for Gatling HTTP DSL
// can be omitted if you don't use jdbcFeeder
// used for specifying durations with a unit, eg Duration.ofMinutes(5)


public class JavaSimulation extends BaseSimulation {

    String configName = getProperty("CONFIG", "config/load.conf");
    Config config = ConfigFactory.load(configName).withFallback(ConfigFactory.load("config/load.conf"));
    int postsNumber;


    ScenarioBuilder writerScn(int postsNumber) {
      return scenario("PostWriterScenario")
              .exec(new PostWriterScnJava().scn(postsNumber));
    }

    ScenarioBuilder readerScn() {
        return scenario("PostsReaderScenario")
                .exec(new PostReaderScnJava().scn());
    }


    List<PopulationBuilder> loadProfile(int rampUpTime, int duration, int minUsers, int maxUsers){
        return List.of(
                writerScn(postsNumber).injectClosed(
                        rampConcurrentUsers(minUsers).to(maxUsers/2).during(rampUpTime),
                        constantConcurrentUsers(maxUsers/2).during(duration)
                ),
                readerScn().injectClosed(
                        rampConcurrentUsers(minUsers).to(maxUsers).during(rampUpTime),
                        constantConcurrentUsers(maxUsers).during(duration)
                )
        );
    }


    public JavaSimulation() {
        var rampToUsers = config.getInt("performance.rampToUsers");
        var rampUpTime = config.getInt("performance.rampUpTime");
        var duration = config.getInt("performance.duration");
        var maxDuration = config.getInt("performance.maxDuration");
        var respTimeMax = config.getInt("performance.global.assertions.responseTime.max.lte");
        var successPercentage = config.getDouble("performance.global.assertions.successfulRequests.percent.is");

        postsNumber = config.getInt("performance.scenario.postsNumber");

        setUp(
                //readerScn.injectOpen(atOnceUsers(1))
                //writerScn.injectOpen(atOnceUsers(1))
                loadProfile(rampUpTime, duration, 1, rampToUsers)
        ).maxDuration(maxDuration)
                .protocols(httpConf)
                .assertions(
                        global().responseTime().max().lte(respTimeMax),
                        global().successfulRequests().percent().is(successPercentage)
                );
    }

    @Override
    public void before() {
        System.out.println("Simulation started!");
    }

    @Override
    public void after() {
        System.out.println("Simulation is finished!");
    }

}
