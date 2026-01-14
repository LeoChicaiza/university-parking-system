package com.university.parking.lot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "parking_lots")
public class ParkingLot {

    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int occupied;

    protected ParkingLot() {
        // JPA
    }

    public ParkingLot(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.occupied = 0;
    }

    public boolean occupy() {
        if (occupied >= capacity) {
            return false;
        }
        occupied++;
        return true;
    }

    public void release() {
        if (occupied > 0) {
            occupied--;
        }
    }

    // Getters

    public String getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getOccupied() {
        return occupied;
    }
}

