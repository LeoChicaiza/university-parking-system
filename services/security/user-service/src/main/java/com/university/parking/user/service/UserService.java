package com.university.parking.user.service;

import com.university.parking.user.model.Role;
import com.university.parking.user.model.User;
import com.university.parking.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(String email, String name, String role) {
        // Convertir String a Role enum - ESTO LANZARÁ IllegalArgumentException si el rol no es válido
        Role userRole = Role.valueOf(role.toUpperCase());
        
        return repository.save(new User(email, name, userRole));
    }

    public User getUser(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean isUserActive(String email) {
        return getUser(email).isActive();
    }

    public String getUserRole(String email) {
        return getUser(email).getRole().name();
    }

    public void deactivateUser(String email) {
        User user = getUser(email);
        user.deactivate();
        repository.save(user);
    }
}