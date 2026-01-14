package com.university.parking.auth.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String USER_SERVICE_URL = "http://localhost:8086/users/";

    public Map<String, Object> getUserByEmail(String email) {
        return restTemplate.getForObject(
                USER_SERVICE_URL + email,
                Map.class
        );
    }
}
