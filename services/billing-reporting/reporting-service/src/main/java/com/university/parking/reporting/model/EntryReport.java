package com.university.parking.reporting.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "entry_reports")
public class EntryReport {

    @Id
    @GeneratedValue
    private UUID id;

    private String plate;
    private LocalDateTime entryTime;

    protected EntryReport() {}

    public EntryReport(String plate, LocalDateTime entryTime) {
        this.plate = plate;
        this.entryTime = entryTime;
    }

    // Getters públicos
    public UUID getId() {
        return id;
    }
    
    public String getPlate() {
        return plate;
    }
    
    public LocalDateTime getEntryTime() {
        return entryTime;
    }
}
