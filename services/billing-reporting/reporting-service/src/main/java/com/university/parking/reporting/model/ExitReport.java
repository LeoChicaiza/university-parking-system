package com.university.parking.reporting.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "exit_reports")
public class ExitReport {

    @Id
    @GeneratedValue
    private UUID id;

    private String plate;
    private LocalDateTime exitTime;

    protected ExitReport() {}

    public ExitReport(String plate, LocalDateTime exitTime) {
        this.plate = plate;
        this.exitTime = exitTime;
    }

    // Getters públicos
    public UUID getId() {
        return id;
    }
    
    public String getPlate() {
        return plate;
    }
    
    public LocalDateTime getExitTime() {
        return exitTime;
    }
}