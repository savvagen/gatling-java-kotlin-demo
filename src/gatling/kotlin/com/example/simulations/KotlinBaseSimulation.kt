import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl


abstract class KotlinBaseSimulation: Simulation() {

    fun getProperty(name: String?, default: String): String = System.getenv(name) ?: default

    var httpConf = HttpDsl.http.baseUrl("http://localhost:3001")
        .contentTypeHeader("application/json; charset=utf8")
        .acceptCharsetHeader("application/json")
        .disableWarmUp()
        .disableCaching()

}