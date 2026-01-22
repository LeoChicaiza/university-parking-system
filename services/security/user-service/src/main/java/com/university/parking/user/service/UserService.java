package com.university.parking.user.service;

import com.university.parking.user.model.User;
import com.university.parking.user.model.Role;
import com.university.parking.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    // MANTÉN ESTE MÉTODO para compatibilidad con tests
    public User createUser(String email, String name, String role) {
        Role roleEnum;
        try {
            roleEnum = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + role);
        }
        
        User user = new User(email, name, roleEnum);
        return repository.save(user);
    }

    // MÉTODO NUEVO para el controlador actualizado
    public User createUser(String email, String name, Role role) {
        User user = new User(email, name, role);
        return repository.save(user);
    }

    public User getUser(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    public String getUserRole(String email) {
        User user = getUser(email);
        return user.getRole().name();
    }

    public boolean isUserActive(String email) {
        User user = getUser(email);
        return user.isActive();
    }

    public void deactivateUser(String email) {
        User user = getUser(email);
        user.deactivate();
        repository.save(user);
    }

    public boolean validateCredentials(String email, String password) {
        // DEMO: En producción usar BCrypt
        Optional<User> userOpt = repository.findByEmail(email);
        
        if (userOpt.isEmpty() || !userOpt.get().isActive()) {
            return false;
        }
        
        // Validación simple para demo
        // En producción: usar BCryptPasswordEncoder
        return "password".equals(password);
    }

    public Iterable<User> getAllUsers() {
        return repository.findAll();
    }
}