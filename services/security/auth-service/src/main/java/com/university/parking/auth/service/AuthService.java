package com.university.parking.auth.service;

import com.university.parking.auth.client.UserServiceClient;
import com.university.parking.auth.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UserServiceClient userClient;
    private final JwtUtil jwtUtil;

    public AuthService(UserServiceClient userClient, JwtUtil jwtUtil) {
        this.userClient = userClient;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, String> login(String email, String password) {

        // 1️⃣ Obtener usuario desde User Service
        Map<String, Object> user = userClient.getUserByEmail(email);

        if (!(Boolean) user.get("active")) {
            throw new RuntimeException("User is inactive");
        }

        // 2️⃣ Validación simple (demo académica)
        if (!"password".equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }

        // 3️⃣ Generar JWT
        String role = (String) user.get("role");
        String token = jwtUtil.generateToken(email, role);

        return Map.of(
                "token", token,
                "role", role
        );
    }
}
