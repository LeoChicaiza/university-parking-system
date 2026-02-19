package com.university.parking.user.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "security")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // <-- Especificar estrategia UUID
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    // Constructor por defecto protegido - NO CAMBIAR
    protected User() {}

    // Constructor con parámetros - NO CAMBIAR
    public User(String email, String name, Role role) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.active = true;
    }

    // Getters - NO CAMBIAR
    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public Role getRole() { return role; }
    public boolean isActive() { return active; }

    // Setter para id - NO CAMBIAR
    public void setId(UUID id) { this.id = id; }
    
    // Método deactivate - NO CAMBIAR
    public void deactivate() {
        this.active = false;
    }
    
    // Métodos adicionales útiles (opcionales pero recomendados)
    public void activate() {
        this.active = true;
    }
    
    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }
    
    public void updateName(String newName) {
        this.name = newName;
    }
    
    public void updateRole(Role newRole) {
        this.role = newRole;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", active=" + active +
                '}';
    }
}