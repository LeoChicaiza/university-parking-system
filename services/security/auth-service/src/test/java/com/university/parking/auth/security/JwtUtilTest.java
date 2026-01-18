package com.university.parking.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        
        // Usar la misma clave que en JwtUtil
        String secret = "my-super-secret-key-for-jwt-256-bits-123!";
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void generateToken_ShouldCreateValidJWT() {
        // Arrange
        String email = "test@university.edu";
        String role = "USER";

        // Act
        String token = jwtUtil.generateToken(email, role);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // Verificar que el token puede ser parseado
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
            
            assertEquals(email, claims.getSubject());
            assertEquals(role, claims.get("role", String.class));
            assertNotNull(claims.getIssuedAt());
            assertNotNull(claims.getExpiration());
            assertTrue(claims.getExpiration().after(new Date()));
        } catch (Exception e) {
            fail("Token should be valid and parsable: " + e.getMessage());
        }
    }

    @Test
    void generateToken_WithDifferentRoles_ShouldIncludeRoleInClaim() {
        // Arrange
        String email = "admin@university.edu";
        String[] roles = {"ADMIN", "USER", "OPERATOR"};

        for (String role : roles) {
            // Act
            String token = jwtUtil.generateToken(email, role);

            // Assert
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
            
            assertEquals(role, claims.get("role", String.class), 
                "Token should contain role: " + role);
        }
    }

    @Test
    void generateToken_ShouldHaveOneHourExpiration() {
        // Arrange
        String email = "test@university.edu";
        String role = "USER";
        long oneHourInMillis = 3600000;
        long tolerance = 5000; // 5 seconds tolerance

        // Act
        String token = jwtUtil.generateToken(email, role);

        // Assert
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
        
        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();
        
        long actualDuration = expiration.getTime() - issuedAt.getTime();
        long difference = Math.abs(actualDuration - oneHourInMillis);
        
        assertTrue(difference <= tolerance, 
            "Token should expire in approximately 1 hour. Difference: " + difference + "ms");
    }

    @Test
    void generateToken_WithEmptyEmail_ShouldStillCreateToken() {
        // Arrange
        String emptyEmail = "";
        String role = "USER";

        // Act
        String token = jwtUtil.generateToken(emptyEmail, role);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // Verificar usando el método extractUsername de JwtUtil
        String extractedEmail = jwtUtil.extractUsername(token);
        assertEquals("", extractedEmail, "Extracted email should be empty string");
        
        // También verificar usando JWT parser directamente
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
        
        String subject = claims.getSubject();
        assertNotNull(subject, "Subject should not be null");
        assertEquals("", subject, "Subject should be empty string");
    }

    @Test
    void generateToken_WithNullRole_ShouldHandleGracefully() {
        // Arrange
        String email = "test@university.edu";
        String nullRole = null;

        // Act
        String token = jwtUtil.generateToken(email, nullRole);

        // Assert
        assertNotNull(token);
        
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
        
        assertNull(claims.get("role"), "Role claim should be null");
        assertEquals(email, claims.getSubject(), "Email should be preserved");
    }
    
    @Test
    void extractUsername_ShouldReturnCorrectEmail() {
        // Arrange
        String email = "student@university.edu";
        String role = "STUDENT";
        String token = jwtUtil.generateToken(email, role);
        
        // Act
        String extractedEmail = jwtUtil.extractUsername(token);
        
        // Assert
        assertEquals(email, extractedEmail);
    }
    
    @Test
    void extractUsername_WithEmptyEmail_ShouldReturnEmptyString() {
        // Arrange
        String token = jwtUtil.generateToken("", "USER");
        
        // Act
        String extractedEmail = jwtUtil.extractUsername(token);
        
        // Assert
        assertEquals("", extractedEmail);
    }
    
    @Test
    void extractRole_ShouldReturnCorrectRole() {
        // Arrange
        String email = "professor@university.edu";
        String role = "PROFESSOR";
        String token = jwtUtil.generateToken(email, role);
        
        // Act
        String extractedRole = jwtUtil.extractRole(token);
        
        // Assert
        assertEquals(role, extractedRole);
    }
    
    @Test
    void extractRole_WithNullRole_ShouldReturnNull() {
        // Arrange
        String token = jwtUtil.generateToken("test@university.edu", null);
        
        // Act
        String extractedRole = jwtUtil.extractRole(token);
        
        // Assert
        assertNull(extractedRole);
    }
}