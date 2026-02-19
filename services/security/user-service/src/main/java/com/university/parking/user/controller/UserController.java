package com.university.parking.user.controller;

import com.university.parking.user.model.User;
import com.university.parking.user.model.Role;
import com.university.parking.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // ✅ 1. Crear usuario
    @PostMapping
    public ResponseEntity<User> create(@RequestBody Map<String, String> body) {
        try {
            Role role = Role.valueOf(body.get("role").toUpperCase());
            User user = service.createUser(
                body.get("email"),
                body.get("name"),
                role
            );
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ✅ 2. Obtener usuario por email (para Auth Service)
    @GetMapping("/search/findByEmail")
    public ResponseEntity<User> findByEmail(@RequestParam String email) {
        try {
            User user = service.getUser(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ 3. Obtener usuario por ID (REST estándar)
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable UUID id) {
        try {
            User user = service.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ 4. Endpoint de validación para Auth Service (IMPORTANTE)
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateCredentials(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        // Esto es DEMO - en producción usar BCrypt
        boolean isValid = service.validateCredentials(email, password);
        
        if (isValid) {
            User user = service.getUser(email);
            return ResponseEntity.ok(Map.of(
                "valid", true,
                "userId", user.getId(),
                "email", user.getEmail(),
                "role", user.getRole().name(),
                "active", user.isActive()
            ));
        }
        
        return ResponseEntity.ok(Map.of("valid", false));
    }

    // ✅ 5. Obtener rol
    @GetMapping("/{email}/role")
    public ResponseEntity<Map<String, String>> getRole(@PathVariable String email) {
        try {
            String role = service.getUserRole(email);
            return ResponseEntity.ok(Map.of("role", role));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ 6. Verificar si está activo
    @GetMapping("/{email}/active")
    public ResponseEntity<Map<String, Boolean>> isActive(@PathVariable String email) {
        try {
            boolean active = service.isUserActive(email);
            return ResponseEntity.ok(Map.of("active", active));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ 7. Desactivar usuario
    @PostMapping("/{email}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable String email) {
        try {
            service.deactivateUser(email);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ 8. Listar todos los usuarios (para admin)
    @GetMapping
    public ResponseEntity<Iterable<User>> getAll() {
        return ResponseEntity.ok(service.getAllUsers());
    }
}