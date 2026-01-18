package com.university.parking.user.controller;

import com.university.parking.user.model.Role;
import com.university.parking.user.model.User;
import com.university.parking.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
        
        when(userService.createUser(anyString(), anyString(), anyString()))
            .thenReturn(createdUser);

        // Act
        User result = userController.create(requestBody);

        // Assert
        assertNotNull(result);
        assertEquals("newstudent@university.edu", result.getEmail());
        assertEquals("New Student", result.getName());
        assertEquals(Role.STUDENT, result.getRole());
        
        verify(userService, times(1)).createUser(
            "newstudent@university.edu", 
            "New Student", 
            "STUDENT"
        );
    }

    @Test
    void get_WithExistingEmail_ReturnsUser() {
        // Arrange
        String email = "existing@university.edu";
        User expectedUser = new User(email, "Existing User", Role.TEACHER);
        
        when(userService.getUser(email)).thenReturn(expectedUser);

        // Act
        User result = userController.get(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("Existing User", result.getName());
        assertEquals(Role.TEACHER, result.getRole());
        
        verify(userService, times(1)).getUser(email);
    }

    @Test
    void role_ReturnsUserRoleString() {
        // Arrange
        String email = "admin@university.edu";
        
        when(userService.getUserRole(email)).thenReturn("ADMIN");

        // Act
        String result = userController.role(email);

        // Assert
        assertEquals("ADMIN", result);
        verify(userService, times(1)).getUserRole(email);
    }

    @Test
    void active_ForActiveUser_ReturnsTrue() {
        // Arrange
        String email = "activeuser@university.edu";
        
        when(userService.isUserActive(email)).thenReturn(true);

        // Act
        boolean result = userController.active(email);

        // Assert
        assertTrue(result);
        verify(userService, times(1)).isUserActive(email);
    }

    @Test
    void deactivate_CallsServiceMethod() {
        // Arrange
        String email = "todeactivate@university.edu";

        // Act
        userController.deactivate(email);

        // Assert
        verify(userService, times(1)).deactivateUser(email);
    }
}