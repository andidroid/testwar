package me.andidroid.testwar;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.time.Duration;

public class TestGatlingSimulation extends Simulation {

        HttpProtocolBuilder httpProtocol = http
                        .baseUrl("http://localhost:8080")
                        // .acceptHeader("text/html,application/xhtml+xml,application/xml,application/json")
                        // // 6
                        .maxConnectionsPerHost(100)
                        .doNotTrackHeader("1");

        ScenarioBuilder scn = scenario("TestGatlingSimulation") // 7
                        .exec(http("test_1") // 8
                                             .get("/testwar/testservice/test/1") // 9
                                        // .get("/testwar/testservice/hello/hello") // 9
                                        .check(status().is(200)));
        // .pause(5); // 10

        {
                setUp( // 11
                       // 10 parallel clients, 1 request
                       // scn.injectOpen(atOnceUsers(10)) // 12

                                // 25 parallel clients, for 10 seconds so many requests as possible
                                scn.injectClosed(CoreDsl.constantConcurrentUsers(25).during(Duration.ofSeconds(100))) // 12
                ).protocols(httpProtocol); // 13
        }

}
