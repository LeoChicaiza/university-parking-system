package com.university.parking.auth.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "jwt.secret=university-parking-system-super-secret-key-256-bits-2026",
    "jwt.expiration=86400000",
    "jwt.refresh-expiration=604800000"
})
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void generateAccessToken_ShouldCreateValidJWT() {
        // Arrange
        String email = "test@university.edu";
        String role = "USER";
        String userId = UUID.randomUUID().toString();

        // Act
        String token = jwtUtil.generateAccessToken(email, role, userId);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // Verificar que el token puede ser parseado
        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            
            assertEquals(email, claims.getSubject());
            assertEquals(role, claims.get("role", String.class));
            assertEquals(userId, claims.get("userId", String.class));
            assertEquals("ACCESS", claims.get("type", String.class));
            assertNotNull(claims.getIssuedAt());
            assertNotNull(claims.getExpiration());
            assertTrue(claims.getExpiration().after(new Date()));
        } catch (Exception e) {
            fail("Token should be valid and parsable: " + e.getMessage());
        }
    }

    @Test
    void generateRefreshToken_ShouldCreateValidJWT() {
        // Arrange
        String email = "test@university.edu";
        String userId = UUID.randomUUID().toString();

        // Act
        String token = jwtUtil.generateRefreshToken(email, userId);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        Claims claims = jwtUtil.extractAllClaims(token);
        
        assertEquals(email, claims.getSubject());
        assertEquals(userId, claims.get("userId", String.class));
        assertEquals("REFRESH", claims.get("type", String.class));
    }

    @Test
    void generateAccessToken_WithDifferentRoles_ShouldIncludeRoleInClaim() {
        // Arrange
        String email = "admin@university.edu";
        String userId = UUID.randomUUID().toString();
        String[] roles = {"ADMIN", "USER", "TEACHER", "VISITOR"};

        for (String role : roles) {
            // Act
            String token = jwtUtil.generateAccessToken(email, role, userId);

            // Assert
            Claims claims = jwtUtil.extractAllClaims(token);
            
            assertEquals(role, claims.get("role", String.class), 
                "Token should contain role: " + role);
            assertEquals("ACCESS", claims.get("type", String.class));
        }
    }

    @Test
    void extractUsername_ShouldReturnCorrectEmail() {
        // Arrange
        String email = "student@university.edu";
        String role = "STUDENT";
        String userId = UUID.randomUUID().toString();
        String token = jwtUtil.generateAccessToken(email, role, userId);
        
        // Act
        String extractedEmail = jwtUtil.extractUsername(token);
        
        // Assert
        assertEquals(email, extractedEmail);
    }
    
    @Test
    void extractRole_ShouldReturnCorrectRole() {
        // Arrange
        String email = "professor@university.edu";
        String role = "TEACHER";
        String userId = UUID.randomUUID().toString();
        String token = jwtUtil.generateAccessToken(email, role, userId);
        
        // Act
        String extractedRole = jwtUtil.extractRole(token);
        
        // Assert
        assertEquals(role, extractedRole);
    }
    
    @Test
    void extractUserId_ShouldReturnCorrectUserId() {
        // Arrange
        String email = "test@university.edu";
        String role = "USER";
        String userId = UUID.randomUUID().toString();
        String token = jwtUtil.generateAccessToken(email, role, userId);
        
        // Act
        String extractedUserId = jwtUtil.extractUserId(token);
        
        // Assert
        assertEquals(userId, extractedUserId);
    }
    
    @Test
    void extractTokenType_AccessToken_ReturnsAccess() {
        // Arrange
        String email = "test@university.edu";
        String role = "ADMIN";
        String userId = UUID.randomUUID().toString();
        String token = jwtUtil.generateAccessToken(email, role, userId);
        
        // Act
        String tokenType = jwtUtil.extractTokenType(token);
        
        // Assert
        assertEquals("ACCESS", tokenType);
    }
    
    @Test
    void extractTokenType_RefreshToken_ReturnsRefresh() {
        // Arrange
        String email = "test@university.edu";
        String userId = UUID.randomUUID().toString();
        String token = jwtUtil.generateRefreshToken(email, userId);
        
        // Act
        String tokenType = jwtUtil.extractTokenType(token);
        
        // Assert
        assertEquals("REFRESH", tokenType);
    }
    
    @Test
    void isTokenValid_ValidToken_ReturnsTrue() {
        // Arrange
        String email = "test@university.edu";
        String role = "VISITOR";
        String userId = UUID.randomUUID().toString();
        String token = jwtUtil.generateAccessToken(email, role, userId);
        
        // Act
        boolean isValid = jwtUtil.isTokenValid(token);
        
        // Assert
        assertTrue(isValid);
    }
    
    @Test
    void extractAllClaims_ValidToken_ReturnsClaims() {
        // Arrange
        String email = "test@university.edu";
        String role = "ADMIN";
        String userId = UUID.randomUUID().toString();
        String token = jwtUtil.generateAccessToken(email, role, userId);
        
        // Act
        Claims claims = jwtUtil.extractAllClaims(token);
        
        // Assert
        assertNotNull(claims);
        assertEquals(email, claims.getSubject());
        assertEquals(role, claims.get("role", String.class));
        assertEquals(userId, claims.get("userId", String.class));
        assertEquals("ACCESS", claims.get("type", String.class));
    }
}