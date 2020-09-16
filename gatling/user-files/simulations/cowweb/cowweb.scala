package cowweb

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class cowweb extends Simulation {

  object Configuration {
    var SCENARIO_NAME = "Default Moo!"
    var REQUEST_NAME = "Default Moo!"
    var PATH = "/say"

    val BASE_URL = System.getProperty("baseUrl", "http://localhost:8080")
    val USERS : Long = java.lang.Long.valueOf(System.getProperty("users", "12"))
    val DURING : Int = java.lang.Integer.valueOf(System.getProperty("during", "12"))

    val URL = BASE_URL + PATH
  }

  val get2productpage = scenario(Configuration.SCENARIO_NAME)
    .exec(
      http(Configuration.REQUEST_NAME)
        .get(Configuration.URL)
    ).pause(1)

  var httpProtocol = http
    .acceptHeader("application/json")
    .header("Cache-Control", "max-age=0")
    .connectionHeader("keep-alive")

  setUp(
    get2productpage.inject(Seq(
      rampUsersPerSec(1).to(Configuration.USERS).during(Configuration.DURING / 3),
      constantUsersPerSec(Configuration.USERS) during(Configuration.DURING / 3),
      rampUsersPerSec(Configuration.USERS).to(1).during(Configuration.DURING / 3)
    ))
  ).protocols(httpProtocol)

}