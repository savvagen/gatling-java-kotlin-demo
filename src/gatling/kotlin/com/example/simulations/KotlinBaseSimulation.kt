import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl


abstract class KotlinBaseSimulation: Simulation() {

    fun getProperty(name: String?, default: String): String = System.getenv(name) ?: default


    private val baseUrl = getProperty("BASE_URL", "http://localhost:3001")

    val httpConf = HttpDsl.http.baseUrl(baseUrl)
        .userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36")
        .contentTypeHeader("application/json; charset=utf-8")
        .acceptHeader("application/json")
        .acceptCharsetHeader("utf-8")
        .warmUp(baseUrl) //.disableWarmUp()
        .disableCaching()

}