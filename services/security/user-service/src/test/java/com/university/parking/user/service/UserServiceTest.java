package com.university.parking.user.service;

import com.university.parking.user.model.Role;
import com.university.parking.user.model.User;
import com.university.parking.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_WithValidRole_CreatesUserSuccessfully() {
        // Arrange
        String email = "student@university.edu";
        String name = "John Doe";
        String role = "STUDENT";
        
        User savedUser = new User(email, name, Role.STUDENT);
        savedUser.setId(UUID.randomUUID());
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.createUser(email, name, role);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(name, result.getName());
        assertEquals(Role.STUDENT, result.getRole());
        assertTrue(result.isActive());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_WithRoleEnum_CreatesUserSuccessfully() {
        // Arrange
        String email = "teacher@university.edu";
        String name = "Jane Doe";
        Role role = Role.TEACHER;
        
        User savedUser = new User(email, name, role);
        savedUser.setId(UUID.randomUUID());
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.createUser(email, name, role);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(name, result.getName());
        assertEquals(role, result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUser_WhenUserExists_ReturnsUser() {
        // Arrange
        String email = "existing@university.edu";
        User expectedUser = new User(email, "Existing User", Role.TEACHER);
        expectedUser.setId(UUID.randomUUID());
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        // Act
        User result = userService.getUser(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("Existing User", result.getName());
        assertEquals(Role.TEACHER, result.getRole());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void getUser_WhenUserNotExists_ThrowsRuntimeException() {
        // Arrange
        String email = "nonexistent@university.edu";
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userService.getUser(email)
        );
        
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void getUserRole_ReturnsRoleNameAsString() {
        // Arrange
        String email = "admin@university.edu";
        User adminUser = new User(email, "Admin User", Role.ADMIN);
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(adminUser));

        // Act
        String result = userService.getUserRole(email);

        // Assert
        assertEquals("ADMIN", result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void isUserActive_ForNewUser_ReturnsTrue() {
        // Arrange
        String email = "newuser@university.edu";
        User newUser = new User(email, "New User", Role.STUDENT);
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(newUser));

        // Act
        boolean result = userService.isUserActive(email);

        // Assert
        assertTrue(result, "New users should be active by default");
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void isUserActive_ForDeactivatedUser_ReturnsFalse() {
        // Arrange
        String email = "deactivated@university.edu";
        User deactivatedUser = new User(email, "Deactivated User", Role.VISITOR);
        deactivatedUser.deactivate();
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(deactivatedUser));

        // Act
        boolean result = userService.isUserActive(email);

        // Assert
        assertFalse(result, "Deactivated user should return false");
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void deactivateUser_CallsDeactivateAndSave() {
        // Arrange
        String email = "todeactivate@university.edu";
        User user = new User(email, "User to Deactivate", Role.VISITOR);
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        userService.deactivateUser(email);

        // Assert
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(user);
    }
    
    @Test
    void createUser_WithInvalidRole_ThrowsRuntimeException() {
        // Arrange
        String email = "test@university.edu";
        String name = "Test User";
        String invalidRole = "INVALID_ROLE";
        
        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userService.createUser(email, name, invalidRole)
        );
        
        assertEquals("Invalid role: INVALID_ROLE", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void createUser_WithLowerCaseRole_ShouldWork() {
        // Arrange
        String email = "visitor@university.edu";
        String name = "Visitor User";
        String role = "visitor"; // minúsculas
        
        User savedUser = new User(email, name, Role.VISITOR);
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.createUser(email, name, role);

        // Assert
        assertNotNull(result);
        assertEquals(Role.VISITOR, result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void createUser_WithMixedCaseRole_ShouldWork() {
        // Arrange
        String email = "teacher@university.edu";
        String name = "Teacher User";
        String role = "TeAcHeR"; // mayúsculas y minúsculas mezcladas
        
        User savedUser = new User(email, name, Role.TEACHER);
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.createUser(email, name, role);

        // Assert
        assertNotNull(result);
        assertEquals(Role.TEACHER, result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void getUserById_WhenUserExists_ReturnsUser() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User expectedUser = new User("test@university.edu", "Test User", Role.STUDENT);
        expectedUser.setId(userId);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
    }
    
    @Test
    void validateCredentials_WithValidUser_ReturnsTrue() {
        // Arrange
        String email = "valid@university.edu";
        User user = new User(email, "Valid User", Role.STUDENT);
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.validateCredentials(email, "password");

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(email);
    }
    
    @Test
    void validateCredentials_WithInvalidPassword_ReturnsFalse() {
        // Arrange
        String email = "valid@university.edu";
        User user = new User(email, "Valid User", Role.STUDENT);
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.validateCredentials(email, "wrongpassword");

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findByEmail(email);
    }
    
    @Test
    void validateCredentials_WithInactiveUser_ReturnsFalse() {
        // Arrange
        String email = "inactive@university.edu";
        User user = new User(email, "Inactive User", Role.STUDENT);
        user.deactivate();
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.validateCredentials(email, "password");

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findByEmail(email);
    }
    
    @Test
    void getAllUsers_ReturnsAllUsers() {
        // Arrange
        List<User> expectedUsers = List.of(
            new User("user1@university.edu", "User 1", Role.STUDENT),
            new User("user2@university.edu", "User 2", Role.TEACHER)
        );
        
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        Iterable<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(expectedUsers, result);
        verify(userRepository, times(1)).findAll();
    }
}