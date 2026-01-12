package com.university.parking.user.model;

public class User {

    private String email;
    private String name;
    private String role;      // ADMIN, STUDENT, STAFF
    private boolean active;

    public User(String email, String name, String role) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.active = true;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }
}
