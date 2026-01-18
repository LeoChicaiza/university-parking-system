package com.university.parking.gateway.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        jwtAuthFilter = new JwtAuthFilter();
    }

    @Test
    void filter_WhenPathStartsWithAuth_ShouldAllowWithoutToken() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/auth/login")
                .build();
        
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // Act & Assert
        StepVerifier.create(jwtAuthFilter.filter(exchange, exchange2 -> Mono.empty()))
                .expectComplete()
                .verify();
        
        assertNull(exchange.getResponse().getStatusCode());
    }

    @Test
    void filter_WhenProtectedPathWithoutToken_ShouldReturnUnauthorized() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/users/profile")
                .build();
        
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // Act & Assert
        StepVerifier.create(jwtAuthFilter.filter(exchange, exchange2 -> Mono.empty()))
                .expectComplete()
                .verify();
        
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void filter_WhenTokenWithoutBearerPrefix_ShouldReturnUnauthorized() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/users/profile")
                .header(HttpHeaders.AUTHORIZATION, "any.token.here")
                .build();
        
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // Act & Assert
        StepVerifier.create(jwtAuthFilter.filter(exchange, exchange2 -> Mono.empty()))
                .expectComplete()
                .verify();
        
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void filter_WhenAuthorizationHeaderIsEmpty_ShouldReturnUnauthorized() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/billing/invoices")
                .header(HttpHeaders.AUTHORIZATION, "")
                .build();
        
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // Act & Assert
        StepVerifier.create(jwtAuthFilter.filter(exchange, exchange2 -> Mono.empty()))
                .expectComplete()
                .verify();
        
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void filter_WhenAuthorizationHeaderIsBearerOnly_ShouldReturnUnauthorized() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/notifications/alerts")
                .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                .build();
        
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // Act & Assert
        StepVerifier.create(jwtAuthFilter.filter(exchange, exchange2 -> Mono.empty()))
                .expectComplete()
                .verify();
        
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void filter_ShouldAllowAuthPathsWithoutToken() {
        String[] publicPaths = {
            "/auth/login",
            "/auth/register",
            "/auth/refresh",
            "/auth/validate"
        };

        for (String path : publicPaths) {
            MockServerHttpRequest request = MockServerHttpRequest.get(path).build();
            ServerWebExchange exchange = MockServerWebExchange.from(request);
            
            jwtAuthFilter.filter(exchange, exchange2 -> Mono.empty()).block();
            
            assertNull(exchange.getResponse().getStatusCode(),
                    "Path " + path + " should allow without authentication");
        }
    }

    @Test
    void filter_ShouldRequireTokenForProtectedPaths() {
        String[] protectedPaths = {
            "/users/profile",
            "/vehicles/list",
            "/parking-spaces/available",
            "/parking-lots/status",
            "/billing/invoices",
            "/notifications/alerts",
            "/reports/monthly"
        };

        for (String path : protectedPaths) {
            MockServerHttpRequest request = MockServerHttpRequest.get(path).build();
            ServerWebExchange exchange = MockServerWebExchange.from(request);
            
            jwtAuthFilter.filter(exchange, exchange2 -> Mono.empty()).block();
            
            assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode(),
                    "Path " + path + " should require authentication");
        }
    }
}