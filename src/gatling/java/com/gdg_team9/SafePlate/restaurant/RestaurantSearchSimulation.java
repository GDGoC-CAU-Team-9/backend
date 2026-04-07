package com.gdg_team9.SafePlate.restaurant;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

/**
 * 부하 테스트: 식당 검색 API (/restaurant/search)
 * <p>
 * 시나리오: 200개의 검색 요청을 동시에 보내고 응답 시간과 성공 여부를 측정
 * <p>
 * 실행 방법: ./gradlew gatlingRun
 * --simulation=com.gdg_team9.SafePlate.restaurant.RestaurantSearchSimulation
 * <p>
 * 실행 전 검색 요청 바디가 적절한지 확인 필요
 */
public class RestaurantSearchSimulation extends Simulation {

    // ============ HTTP 설정 ============
    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .userAgentHeader("Gatling/RestaurantSearch");

    // ============ 검색 요청 바디 ============
    // 사용 전 검색 요청 바디가 적절한지 확인
    private final String loginRequestBody = """
            {
                "email": "a@b.com",
                "password": "12345678"
            }
            """;
    private final String searchRequestBody = """
            {
                "ids": [5],
                "menuLang": "ko"
            }
            """;

    // ============ 시나리오 정의 ============
    private final ScenarioBuilder searchScenario = scenario("Restaurant Search Load Test")
            .exec(
                    http("POST /login")
                            .post("/auth/login")
                            .body(StringBody(loginRequestBody))
                            .requestTimeout(Duration.ofSeconds(10))
                            .check(status().is(200))
                            .check(jsonPath("$.isSuccess").is("true"))
                            .check(jsonPath("$.result.token").exists())
                            .check(jsonPath("$.result.token").saveAs("authToken"))
            )
            .pause(Duration.ofSeconds(3))  // 로그인 후 3초 대기
            .exec(
                    http("POST /restaurant/search")
                            .post("/restaurant/search")
                            .header("Authorization", "Bearer #{authToken}")
                            .body(StringBody(searchRequestBody))
                            .requestTimeout(Duration.ofSeconds(140))
                            .check(status().is(200))
                            .check(responseTimeInMillis().saveAs("responseTime"))
            )
            .pause(Duration.ofMillis(500));  // 요청 간 500ms 대기

    // ============ 부하 테스트 설정 ============
    {
        setUp(
                searchScenario.injectOpen(
                                atOnceUsers(200) // 요청 200개를 한 번에 보냄
                        )
                        .protocols(httpProtocol)
        ).maxDuration(Duration.ofSeconds(150));  // 최대 테스트 시간 150초
    }
}
