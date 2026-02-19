package com.university.parking.auth.service;

import com.university.parking.auth.client.UserServiceClient;
import com.university.parking.auth.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private Map<String, Object> mockUser;
    private String userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID().toString();
        mockUser = Map.of(
            "active", true,
            "role", "USER",
            "email", "test@university.edu",
            "userId", userId,
            "valid", true
        );
    }

    @Test
    void login_ValidCredentials_ShouldReturnTokens() {
        // Arrange
        String email = "test@university.edu";
        String password = "password";
        String expectedAccessToken = "access-token";
        String expectedRefreshToken = "refresh-token";
        
        when(userServiceClient.validateCredentials(email, password)).thenReturn(mockUser);
        when(jwtUtil.generateAccessToken(email, "USER", userId)).thenReturn(expectedAccessToken);
        when(jwtUtil.generateRefreshToken(email, userId)).thenReturn(expectedRefreshToken);

        // Act
        Map<String, Object> result = authService.login(email, password);

        // Assert
        assertNotNull(result);
        assertEquals(expectedAccessToken, result.get("accessToken"));
        assertEquals(expectedRefreshToken, result.get("refreshToken"));
        assertEquals("Bearer", result.get("tokenType"));
        assertEquals("USER", result.get("role"));
        assertEquals(userId, result.get("userId"));
        assertEquals(email, result.get("email"));
        
        verify(userServiceClient, times(1)).validateCredentials(email, password);
        verify(jwtUtil, times(1)).generateAccessToken(email, "USER", userId);
        verify(jwtUtil, times(1)).generateRefreshToken(email, userId);
    }

    @Test
    void login_InvalidCredentials_ShouldThrowException() {
        // Arrange
        String email = "test@university.edu";
        String password = "wrongpassword";
        
        Map<String, Object> invalidUser = Map.of("valid", false);
        when(userServiceClient.validateCredentials(email, password)).thenReturn(invalidUser);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> authService.login(email, password));
        
        assertEquals("Invalid credentials", exception.getMessage());
        
        verify(userServiceClient, times(1)).validateCredentials(email, password);
        verify(jwtUtil, never()).generateAccessToken(anyString(), anyString(), anyString());
        verify(jwtUtil, never()).generateRefreshToken(anyString(), anyString());
    }

    @Test
    void login_UserInactive_ShouldThrowException() {
        // Arrange
        String email = "inactive@university.edu";
        String password = "password";
        
        Map<String, Object> inactiveUser = Map.of(
            "valid", true,
            "active", false,
            "role", "USER",
            "email", email,
            "userId", UUID.randomUUID().toString()
        );
        
        when(userServiceClient.validateCredentials(email, password)).thenReturn(inactiveUser);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> authService.login(email, password));
        
        assertEquals("User is inactive", exception.getMessage());
        
        verify(userServiceClient, times(1)).validateCredentials(email, password);
        verify(jwtUtil, never()).generateAccessToken(anyString(), anyString(), anyString());
        verify(jwtUtil, never()).generateRefreshToken(anyString(), anyString());
    }

    @Test
    void login_WithAdminRole_ShouldReturnAdminTokens() {
        // Arrange
        String email = "admin@university.edu";
        String password = "password";
        String adminUserId = UUID.randomUUID().toString();
        String expectedAccessToken = "admin-access-token";
        String expectedRefreshToken = "admin-refresh-token";
        
        Map<String, Object> adminUser = Map.of(
            "valid", true,
            "active", true,
            "role", "ADMIN",
            "email", email,
            "userId", adminUserId
        );
        
        when(userServiceClient.validateCredentials(email, password)).thenReturn(adminUser);
        when(jwtUtil.generateAccessToken(email, "ADMIN", adminUserId)).thenReturn(expectedAccessToken);
        when(jwtUtil.generateRefreshToken(email, adminUserId)).thenReturn(expectedRefreshToken);

        // Act
        Map<String, Object> result = authService.login(email, password);

        // Assert
        assertEquals("ADMIN", result.get("role"));
        assertEquals(adminUserId, result.get("userId"));
        verify(jwtUtil, times(1)).generateAccessToken(email, "ADMIN", adminUserId);
        verify(jwtUtil, times(1)).generateRefreshToken(email, adminUserId);
    }

    @Test
    void validateToken_ValidToken_ReturnsValidationResult() {
        // Arrange
        String token = "valid-token";
        String email = "test@university.edu";
        String role = "USER";
        String userId = UUID.randomUUID().toString();
        
        when(jwtUtil.extractUserId(token)).thenReturn(userId);
        when(jwtUtil.extractUsername(token)).thenReturn(email);
        when(jwtUtil.extractRole(token)).thenReturn(role);

        // Act
        Map<String, Object> result = authService.validateToken(token);

        // Assert
        assertNotNull(result);
        assertEquals(true, result.get("valid"));
        assertEquals(userId, result.get("userId"));
        assertEquals(email, result.get("email"));
        assertEquals(role, result.get("role"));
        
        verify(jwtUtil, times(1)).extractUserId(token);
        verify(jwtUtil, times(1)).extractUsername(token);
        verify(jwtUtil, times(1)).extractRole(token);
    }

    @Test
    void validateToken_InvalidToken_ReturnsError() {
        // Arrange
        String token = "invalid-token";
        
        when(jwtUtil.extractUserId(token)).thenThrow(new RuntimeException("Invalid token"));

        // Act
        Map<String, Object> result = authService.validateToken(token);

        // Assert
        assertNotNull(result);
        assertEquals(false, result.get("valid"));
        assertNotNull(result.get("error"));
        
        verify(jwtUtil, times(1)).extractUserId(token);
    }
}