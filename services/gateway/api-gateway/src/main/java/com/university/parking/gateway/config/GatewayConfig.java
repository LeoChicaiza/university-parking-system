package com.university.parking.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        return builder.routes()

                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri("http://auth-service:8080"))

                .route("user-service", r -> r
                        .path("/users/**")
                        .uri("http://user-service:8080"))

                .route("vehicle-service", r -> r
                        .path("/vehicles/**")
                        .uri("http://vehicle-service:8080"))

                .route("entry-service", r -> r
                        .path("/entry/**")
                        .uri("http://entry-service:8080"))

                .route("exit-service", r -> r
                        .path("/exit/**")
                        .uri("http://exit-service:8080"))

                .route("parking-space-service", r -> r
                        .path("/parking-spaces/**")
                        .uri("http://parking-space-service:8080"))

                .route("parking-lot-service", r -> r
                        .path("/parking-lots/**")
                        .uri("http://parking-lot-service:8080"))

                .route("billing-service", r -> r
                        .path("/billing/**")
                        .uri("http://billing-service:8080"))

                .route("notification-service", r -> r
                        .path("/notifications/**")
                        .uri("http://notification-service:8080"))

                .route("reporting-service", r -> r
                        .path("/reports/**")
                        .uri("http://reporting-service:8080"))

                .build();
    }
}
