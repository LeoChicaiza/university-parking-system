package com.university.parking.auth.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;
    private final String userServiceBaseUrl;

    public UserServiceClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${services.user.url}") String userServiceUrl,
            @Value("${services.user.base-path}") String basePath) {
        
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
        
        this.userServiceBaseUrl = userServiceUrl + basePath;
    }

    public Map<String, Object> getUserByEmail(String email) {
        String url = userServiceBaseUrl + "/search/findByEmail?email=" + email;
        
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Map.class
        );
        
        return response.getBody();
    }

    public Map<String, Object> validateCredentials(String email, String password) {
        String url = userServiceBaseUrl + "/validate";
        
        Map<String, String> request = Map.of(
            "email", email,
            "password", password
        );
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Map.class
        );
        
        return response.getBody();
    }
}