/*
 * Copyright (c) 2019 Hiroshi Hayakawa <hhiroshell@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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