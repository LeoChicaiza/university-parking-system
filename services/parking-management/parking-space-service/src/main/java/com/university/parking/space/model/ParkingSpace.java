package com.university.parking.space.model;

public class ParkingSpace {

    private String id;
    private String lotId;
    private boolean occupied;

    public ParkingSpace(String id, String lotId) {
        this.id = id;
        this.lotId = lotId;
        this.occupied = false;
    }

    public String getId() {
        return id;
    }

    public String getLotId() {
        return lotId;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public synchronized boolean occupy() {
        if (occupied) return false;
        occupied = true;
        return true;
    }

    public synchronized void release() {
        occupied = false;
    }
}

