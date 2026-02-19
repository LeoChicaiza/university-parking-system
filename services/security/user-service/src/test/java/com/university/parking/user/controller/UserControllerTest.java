package com.university.parking.user.controller;

import com.university.parking.user.model.Role;
import com.university.parking.user.model.User;
import com.university.parking.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void create_WithValidData_ReturnsCreatedUser() {
        // Arrange
        Map<String, String> requestBody = Map.of(
            "email", "newstudent@university.edu",
            "name", "New Student",
            "role", "STUDENT"
        );
        
        User createdUser = new User("newstudent@university.edu", "New Student", Role.STUDENT);
        createdUser.setId(UUID.randomUUID());
        
        when(userService.createUser(anyString(), anyString(), any(Role.class)))
            .thenReturn(createdUser);

        // Act
        ResponseEntity<User> response = userController.create(requestBody);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        User result = response.getBody();
        assertEquals("newstudent@university.edu", result.getEmail());
        assertEquals("New Student", result.getName());
        assertEquals(Role.STUDENT, result.getRole());
        
        verify(userService, times(1)).createUser(
            "newstudent@university.edu", 
            "New Student", 
            Role.STUDENT
        );
    }

    @Test
    void create_WithInvalidRole_ReturnsBadRequest() {
        // Arrange
        Map<String, String> requestBody = Map.of(
            "email", "test@university.edu",
            "name", "Test User",
            "role", "INVALID_ROLE"
        );
        
        // Act
        ResponseEntity<User> response = userController.create(requestBody);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).createUser(anyString(), anyString(), any(Role.class));
    }

    @Test
    void findByEmail_WithExistingEmail_ReturnsUser() {
        // Arrange
        String email = "existing@university.edu";
        User expectedUser = new User(email, "Existing User", Role.TEACHER);
        expectedUser.setId(UUID.randomUUID());
        
        when(userService.getUser(email)).thenReturn(expectedUser);

        // Act
        ResponseEntity<User> response = userController.findByEmail(email);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        User result = response.getBody();
        assertEquals(email, result.getEmail());
        assertEquals("Existing User", result.getName());
        assertEquals(Role.TEACHER, result.getRole());
        
        verify(userService, times(1)).getUser(email);
    }

    @Test
    void findByEmail_WithNonExistingEmail_ReturnsNotFound() {
        // Arrange
        String email = "nonexisting@university.edu";
        
        when(userService.getUser(email)).thenThrow(new RuntimeException("User not found"));

        // Act
        ResponseEntity<User> response = userController.findByEmail(email);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).getUser(email);
    }

    @Test
    void getById_WithExistingId_ReturnsUser() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User expectedUser = new User("test@university.edu", "Test User", Role.STUDENT);
        expectedUser.setId(userId);
        
        when(userService.getUserById(userId)).thenReturn(expectedUser);

        // Act
        ResponseEntity<User> response = userController.getById(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        User result = response.getBody();
        assertEquals(userId, result.getId());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void validateCredentials_WithValidCredentials_ReturnsValidResponse() {
        // Arrange
        Map<String, String> credentials = Map.of(
            "email", "valid@university.edu",
            "password", "password"
        );
        
        User user = new User("valid@university.edu", "Valid User", Role.STUDENT);
        user.setId(UUID.randomUUID());
        
        when(userService.validateCredentials("valid@university.edu", "password")).thenReturn(true);
        when(userService.getUser("valid@university.edu")).thenReturn(user);

        // Act
        ResponseEntity<Map<String, Object>> response = userController.validateCredentials(credentials);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> result = response.getBody();
        assertTrue((Boolean) result.get("valid"));
        assertEquals(user.getId(), result.get("userId"));
        assertEquals("valid@university.edu", result.get("email"));
        assertEquals("STUDENT", result.get("role"));
        assertTrue((Boolean) result.get("active"));
        
        verify(userService, times(1)).validateCredentials("valid@university.edu", "password");
        verify(userService, times(1)).getUser("valid@university.edu");
    }

    @Test
    void getRole_WithExistingEmail_ReturnsRole() {
        // Arrange
        String email = "admin@university.edu";
        
        when(userService.getUserRole(email)).thenReturn("ADMIN");

        // Act
        ResponseEntity<Map<String, String>> response = userController.getRole(email);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, String> result = response.getBody();
        assertEquals("ADMIN", result.get("role"));
        verify(userService, times(1)).getUserRole(email);
    }

    @Test
    void isActive_ForActiveUser_ReturnsTrue() {
        // Arrange
        String email = "activeuser@university.edu";
        
        when(userService.isUserActive(email)).thenReturn(true);

        // Act
        ResponseEntity<Map<String, Boolean>> response = userController.isActive(email);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Boolean> result = response.getBody();
        assertTrue(result.get("active"));
        verify(userService, times(1)).isUserActive(email);
    }

    @Test
    void deactivate_CallsServiceMethod() {
        // Arrange
        String email = "todeactivate@university.edu";

        // Act
        ResponseEntity<Void> response = userController.deactivate(email);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).deactivateUser(email);
    }

    @Test
    void getAll_ReturnsAllUsers() {
        // Arrange
        Iterable<User> users = java.util.List.of(
            new User("user1@university.edu", "User 1", Role.STUDENT),
            new User("user2@university.edu", "User 2", Role.TEACHER)
        );
        
        when(userService.getAllUsers()).thenReturn(users);

        // Act
        ResponseEntity<Iterable<User>> response = userController.getAll();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Iterable<User> result = response.getBody();
        assertNotNull(result);
        verify(userService, times(1)).getAllUsers();
    }
}