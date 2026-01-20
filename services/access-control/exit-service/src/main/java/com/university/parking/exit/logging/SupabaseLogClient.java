package com.university.parking.exit.logging;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class SupabaseLogClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String url;
    private final String apiKey;
    private final String serviceName;

    public SupabaseLogClient(String url, String apiKey, String serviceName) {
        this.url = url;
        this.apiKey = apiKey;
        this.serviceName = serviceName;
    }

    public void info(String message, Object payload) {
        send("INFO", message, payload);
    }

    public void error(String message, Object payload) {
        send("ERROR", message, payload);
    }

    private void send(String level, String message, Object payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", apiKey);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> body = Map.of(
                "service_name", serviceName,
                "level", level,
                "message", message,
                "payload", payload
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Void.class
        );
    }
}
