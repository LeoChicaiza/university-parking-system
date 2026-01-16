package com.university.parking.user.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "core_domain")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean active = true;

    protected User() {}

    public User(String email, String name, Role role) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.active = true;
    }

    // Getters
    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public Role getRole() { return role; }
    public boolean isActive() { return active; }

    // Setters mínimos necesarios para pruebas
    public void setId(UUID id) { this.id = id; }
    
    // NO agregues setActive() si no lo necesitas en producción
    // En su lugar, podemos usar deactivate() para cambiar el estado

    public void deactivate() {
        this.active = false;
    }
}