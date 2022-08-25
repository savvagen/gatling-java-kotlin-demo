package com.example.simulations
import KotlinBaseSimulation
import com.example.scenarios.FailOverScenario
import com.example.scenarios.ReaderScenario
import com.example.scenarios.WriterScenario
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.PopulationBuilder


class KotlinLoadSimulation : KotlinBaseSimulation() {

    val config: Config = ConfigFactory.load(getProperty("CONFIG", "config/load.conf"))
        .withFallback(ConfigFactory.load("config/load.conf"))

    val rampToUsers = config.getInt("performance.rampToUsers")
    val rampUpTime = config.getLong("performance.rampUpTime")
    val duration = config.getLong("performance.duration")
    val maxDuration = config.getLong("performance.maxDuration")
    val respTimeMax = config.getInt("performance.global.assertions.responseTime.max.lte")
    val successPercentage = config.getDouble("performance.global.assertions.successfulRequests.percent.is")


    private val readScn = scenario("Reader Scenario")
        .exec(ReaderScenario.scn) // .exec(ReaderScenario().scn)

    private val writerScn = scenario("Writer Scenario")
        .exec(WriterScenario.scn(1))

    private val errorCounterScn = scenario("Error Counter")
        .exec(FailOverScenario.scn())




    private fun loadProfile(rampUpTime: Long, duration: Long, minUsers: Int = 1, maxUsers: Int): List<PopulationBuilder> =
        listOf(
            writerScn.injectClosed(
                rampConcurrentUsers(minUsers).to(maxUsers / 2).during(rampUpTime),
                constantConcurrentUsers(maxUsers / 2).during(duration)
            ),
            readScn.injectClosed(
                rampConcurrentUsers(minUsers).to(maxUsers).during(rampUpTime),
                constantConcurrentUsers(maxUsers).during(duration)
            )
        )

    init {
        setUp(
            //readScn.injectOpen(atOnceUsers(1)),
            //writerScn.injectOpen(atOnceUsers(1)),
            //loadProfile(),
            loadProfile(rampUpTime = rampUpTime, duration =  duration, maxUsers = rampToUsers)
            // ..... profile for error counter .....
            //readScn.injectClosed(constantConcurrentUsers(1).during(duration)),
            //errorCounterScn.injectOpen(constantUsersPerSec(1.0).during(duration))
        ).protocols(httpConf)
            .assertions(
                global().responseTime().max().lte(respTimeMax),
                global().successfulRequests().percent().shouldBe(successPercentage)
            ).maxDuration(maxDuration)
    }
}
