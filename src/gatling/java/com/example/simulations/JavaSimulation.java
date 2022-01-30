package com.example.simulations;

// required for Gatling core structure DSL
import com.example.protocols.HttpProtocol;
import com.example.scenarios.UserReader;
import io.gatling.javaapi.core.*;
import static com.example.protocols.HttpProtocol.*;
import static io.gatling.javaapi.core.CoreDsl.*;
// required for Gatling HTTP DSL
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.http.HttpDsl.*;
// can be omitted if you don't use jdbcFeeder
import io.gatling.javaapi.jdbc.*;
import static io.gatling.javaapi.jdbc.JdbcDsl.*;
// used for specifying durations with a unit, eg Duration.ofMinutes(5)
import java.time.Duration;


public class JavaSimulation extends Simulation {

    ScenarioBuilder scn = scenario("Get Users")
            .exec(new UserReader().scn);

    public JavaSimulation() {
        setUp(scn.injectOpen(atOnceUsers(1)))
                .protocols(httpConf);
    }
}
