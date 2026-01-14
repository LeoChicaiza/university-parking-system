package com.university.parking.space.model;

import jakarta.persistence.*;

@Entity
@Table(name = "parking_spaces")
public class ParkingSpace {

    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(name = "lot_id", nullable = false)
    private String lotId;

    @Column(nullable = false)
    private boolean occupied;

    protected ParkingSpace() {
        // JPA
    }

    public ParkingSpace(String id, String lotId) {
        this.id = id;
        this.lotId = lotId;
        this.occupied = false;
    }

    public boolean occupy() {
        if (occupied) {
            return false;
        }
        occupied = true;
        return true;
    }

    public void release() {
        occupied = false;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public String getLotId() {
        return lotId;
    }

    public String getId() {
        return id;
    }
}

