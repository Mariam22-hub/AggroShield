package com.example.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("predict_service", r -> r.path("/prediction/**")
                        .filters(f -> f.circuitBreaker(cb -> cb
                                .setName("predictCB")
                                .setFallbackUri("forward:/fallback/unmatched"))) // Global fallback for unmatched routes
                        .uri("http://127.0.0.1:8010"))
                .route("user_service", r -> r.path("/users/**")
                        .filters(f -> f.circuitBreaker(cb -> cb
                                .setName("userCB")
                                .setFallbackUri("forward:/fallback/users"))) // User service fallback
                        .uri("http://127.0.0.1:8070"))
                .route("alert_service", r -> r.path("/api/alerts/**")
                        .filters(f -> f.circuitBreaker(cb -> cb
                                .setName("alertCB")
                                .setFallbackUri("forward:/fallback/unmatched"))) // User service fallback
                        .uri("http://localhost:8084"))
//                .route("analyze_service", r -> r.path("/analyze_behavior/**")
//                        .filters(f -> f.circuitBreaker(cb -> cb
//                                .setName("analyzeCB")
////                                .rewritePath("/analyze_behavior/(?<segment>.*)", "/${segment}")
//                                .setFallbackUri("forward:/fallback/unmatched"))) // User service fallback
//                        .uri("http://localhost:8082"))
                .route("sensor_service", r -> r.path("/api/sensor/**")
                        .filters(f -> f.circuitBreaker(cb -> cb
                                .setName("sensorCB")
//                                .rewritePath("/analyze_behavior/(?<segment>.*)", "/${segment}")
                                .setFallbackUri("forward:/fallback/unmatched"))) // User service fallback
                        .uri("http://localhost:8093"))
                .route("report_service", r -> r.path("/api/reports/**")
                        .filters(f -> f.circuitBreaker(cb -> cb
                                .setName("reportCB")
//                                .rewritePath("/analyze_behavior/(?<segment>.*)", "/${segment}")
                                .setFallbackUri("forward:/fallback/unmatched"))) // User service fallback
                        .uri("http://localhost:8086"))
                .route("auth_service", r -> r.path("/api/auth/**")
                        .filters(f -> f.circuitBreaker(cb -> cb
                                .setName("authCB")
                                .setFallbackUri("forward:/fallback/unmatched")))
                        .uri("http://localhost:8070"))
                .route("unmatched-route", r -> r
                        .path("/**") // Matches any request
                        .filters(f -> f
                                .rewritePath("/.*", "/fallback/unmatched")) // Rewrite to global fallback
                        .uri("http://localhost:8080"))
                .build();
    }
}