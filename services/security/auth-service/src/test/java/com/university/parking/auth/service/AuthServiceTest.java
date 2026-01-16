package com.university.parking.auth.service;

import com.university.parking.auth.client.UserServiceClient;
import com.university.parking.auth.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

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

    @BeforeEach
    void setUp() {
        mockUser = new HashMap<>();
        mockUser.put("active", true);
        mockUser.put("role", "USER");
        mockUser.put("email", "test@university.edu");
    }

    @Test
    void login_ValidCredentials_ShouldReturnTokenAndRole() {
        // Arrange
        String email = "test@university.edu";
        String password = "password";
        String expectedToken = "mock-jwt-token";
        
        when(userServiceClient.getUserByEmail(email)).thenReturn(mockUser);
        when(jwtUtil.generateToken(email, "USER")).thenReturn(expectedToken);

        // Act
        Map<String, String> result = authService.login(email, password);

        // Assert
        assertNotNull(result);
        assertEquals(expectedToken, result.get("token"));
        assertEquals("USER", result.get("role"));
        
        verify(userServiceClient, times(1)).getUserByEmail(email);
        verify(jwtUtil, times(1)).generateToken(email, "USER");
    }

    @Test
    void login_UserInactive_ShouldThrowException() {
        // Arrange
        String email = "inactive@university.edu";
        String password = "password";
        
        Map<String, Object> inactiveUser = new HashMap<>();
        inactiveUser.put("active", false);
        inactiveUser.put("role", "USER");
        
        when(userServiceClient.getUserByEmail(email)).thenReturn(inactiveUser);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> authService.login(email, password));
        
        assertEquals("User is inactive", exception.getMessage());
        
        verify(userServiceClient, times(1)).getUserByEmail(email);
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }

    @Test
    void login_InvalidPassword_ShouldThrowException() {
        // Arrange
        String email = "test@university.edu";
        String wrongPassword = "wrongpassword";
        
        when(userServiceClient.getUserByEmail(email)).thenReturn(mockUser);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> authService.login(email, wrongPassword));
        
        assertEquals("Invalid credentials", exception.getMessage());
        
        verify(userServiceClient, times(1)).getUserByEmail(email);
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }

    @Test
    void login_WithDifferentRole_ShouldReturnCorrectRole() {
        // Arrange
        String email = "admin@university.edu";
        String password = "password";
        String expectedToken = "admin-token";
        
        Map<String, Object> adminUser = new HashMap<>();
        adminUser.put("active", true);
        adminUser.put("role", "ADMIN");
        adminUser.put("email", email);
        
        when(userServiceClient.getUserByEmail(email)).thenReturn(adminUser);
        when(jwtUtil.generateToken(email, "ADMIN")).thenReturn(expectedToken);

        // Act
        Map<String, String> result = authService.login(email, password);

        // Assert
        assertEquals("ADMIN", result.get("role"));
        verify(jwtUtil, times(1)).generateToken(email, "ADMIN");
    }
}