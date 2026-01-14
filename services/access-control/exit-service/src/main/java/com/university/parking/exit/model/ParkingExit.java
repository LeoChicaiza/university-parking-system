package com.university.parking.exit.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "parking_exit", schema = "access_domain")
public class ParkingExit {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String plate;

    @Column(nullable = false)
    private UUID entryId;

    @Column(nullable = false)
    private Long exitTime;

    @Column(nullable = false)
    private Long createdAt;

    protected ParkingExit() {}

    public ParkingExit(String plate, UUID entryId, Long exitTime) {
        this.plate = plate;
        this.entryId = entryId;
        this.exitTime = exitTime;
        this.createdAt = System.currentTimeMillis();
    }

    public UUID getId() { return id; }
    public String getPlate() { return plate; }
    public UUID getEntryId() { return entryId; }
    public Long getExitTime() { return exitTime; }
    public Long getCreatedAt() { return createdAt; }
}
