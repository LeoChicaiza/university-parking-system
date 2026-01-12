package com.university.parking.lot.model;

public class ParkingLot {

    private String id;
    private int capacity;
    private int occupied;

    public ParkingLot(String id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.occupied = 0;
    }

    public synchronized boolean occupy() {
        if (occupied >= capacity) return false;
        occupied++;
        return true;
    }

    public synchronized void release() {
        if (occupied > 0) occupied--;
    }

    public int getAvailable() {
        return capacity - occupied;
    }
}
