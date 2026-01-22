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

    public Map<String, Object> login(String email, String password) {
        // 1️⃣ Validar credenciales con User Service
        Map<String, Object> validationResult = userClient.validateCredentials(email, password);
        
        if (validationResult == null || !Boolean.TRUE.equals(validationResult.get("valid"))) {
            throw new RuntimeException("Invalid credentials");
        }

        // 2️⃣ Verificar si el usuario está activo
        if (!Boolean.TRUE.equals(validationResult.get("active"))) {
            throw new RuntimeException("User is inactive");
        }

        // 3️⃣ Extraer datos del usuario
        String userId = (String) validationResult.get("userId");
        String role = (String) validationResult.get("role");
        String userEmail = (String) validationResult.get("email");

        // 4️⃣ Generar tokens
        String accessToken = jwtUtil.generateAccessToken(userEmail, role, userId);
        String refreshToken = jwtUtil.generateRefreshToken(userEmail, userId);

        // 5️⃣ Retornar respuesta
        return Map.of(
            "accessToken", accessToken,
            "refreshToken", refreshToken,
            "tokenType", "Bearer",
            "expiresIn", 86400000,
            "role", role,
            "userId", userId,
            "email", userEmail,
            "message", "Login successful"
        );
    }

    public Map<String, Object> validateToken(String token) {
        try {
            String userId = jwtUtil.extractUserId(token);
            String email = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);
            
            return Map.of(
                "valid", true,
                "userId", userId,
                "email", email,
                "role", role
            );
        } catch (Exception e) {
            return Map.of("valid", false, "error", e.getMessage());
        }
    }
}