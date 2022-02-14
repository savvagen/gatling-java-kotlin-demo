package com.example.simulations;

// required for Gatling core structure DSL
import com.example.scenarios.PostWriterScnJava;
import io.gatling.javaapi.core.*;
import static com.example.protocols.HttpProtocol.*;
import static io.gatling.javaapi.core.CoreDsl.*;
// required for Gatling HTTP DSL
// can be omitted if you don't use jdbcFeeder
// used for specifying durations with a unit, eg Duration.ofMinutes(5)


public class JavaSimulation extends Simulation {

    ScenarioBuilder scn = scenario("Get Users")
            .exec(new PostWriterScnJava().scn);

    public JavaSimulation() {
        setUp(scn.injectOpen(atOnceUsers(1)))
                .protocols(httpConf);
    }
}
