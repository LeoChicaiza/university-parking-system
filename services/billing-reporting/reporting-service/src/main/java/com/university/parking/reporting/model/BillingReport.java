package com.university.parking.reporting.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "billing_reports")
public class BillingReport {

    @Id
    @GeneratedValue
    private UUID id;

    private String plate;
    private double amount;
    private LocalDateTime createdAt;

    protected BillingReport() {}

    public BillingReport(String plate, double amount) {
        this.plate = plate;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

    // Getters públicos
    public UUID getId() {
        return id;
    }
    
    public String getPlate() {
        return plate;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}