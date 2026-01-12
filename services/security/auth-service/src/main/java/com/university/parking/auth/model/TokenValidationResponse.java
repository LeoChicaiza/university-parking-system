package com.university.parking.auth.model;

public class TokenValidationResponse {
    public boolean valid;
    public String email;
    public String role;

    public TokenValidationResponse(boolean valid, String email, String role) {
        this.valid = valid;
        this.email = email;
        this.role = role;
    }
}
