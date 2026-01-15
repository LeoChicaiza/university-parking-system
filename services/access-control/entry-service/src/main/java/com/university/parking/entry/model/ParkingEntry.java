package com.university.parking.entry.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "parking_entry", schema = "access_domain")
public class ParkingEntry {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String plate;

    @Column(nullable = false)
    private String spaceId;

    @Column(nullable = false)
    private String lotId;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private Long entryTime;

    @Enumerated(EnumType.STRING)
    private EntryStatus status;

    protected ParkingEntry() {}

    public ParkingEntry(
            String plate,
            String spaceId,
            String lotId,
            String userEmail,
            Long entryTime
    ) {
        this.plate = plate;
        this.spaceId = spaceId;
        this.lotId = lotId;
        this.userEmail = userEmail;
        this.entryTime = entryTime;
        this.status = EntryStatus.ACTIVE;
    }

    public UUID getId() { return id; }
    public String getPlate() { return plate; }
    public String getSpaceId() { return spaceId; }
    public String getLotId() { return lotId; }
    public String getUserEmail() { return userEmail; }
    public Long getEntryTime() { return entryTime; }
    public EntryStatus getStatus() { return status; }

    public void close() {
        this.status = EntryStatus.CLOSED;
    }
}
