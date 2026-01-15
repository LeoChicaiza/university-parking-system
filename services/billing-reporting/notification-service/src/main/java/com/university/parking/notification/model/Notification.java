package com.university.parking.notification.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue
    private UUID id;

    private String plate;

    private double amount;

    private String message;

    private LocalDateTime createdAt;

    protected Notification() {
        // JPA
    }

    public Notification(String plate, double amount, String message) {
        this.plate = plate;
        this.amount = amount;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getPlate() {
        return plate;
    }

    public double getAmount() {
        return amount;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
