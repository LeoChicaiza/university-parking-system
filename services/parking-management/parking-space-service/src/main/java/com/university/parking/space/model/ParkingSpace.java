package com.university.parking.space.model;

import jakarta.persistence.*;

@Entity
@Table(name = "parking_spaces", schema = "core_domain")
public class ParkingSpace {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;   // ✅ String, como espera el test

    @Column(name = "lot_id", nullable = false)
    private String lotId;

    @Column(nullable = false)
    private boolean occupied;

    @Column(name = "space_number", nullable = false)
    private String spaceNumber;

    @Column(name = "vehicle_plate")
    private String vehiclePlate;

    protected ParkingSpace() {
        // JPA
    }

    // ✅ Constructor usado por TEST
    public ParkingSpace(String id, String lotId) {
        this.id = id;
        this.lotId = lotId;
        this.spaceNumber = id; // coherente con el dominio
        this.occupied = false;
        this.vehiclePlate = null;
    }

    // Constructor alternativo si lo necesitas
    public ParkingSpace(String id, String lotId, String spaceNumber) {
        this.id = id;
        this.lotId = lotId;
        this.spaceNumber = spaceNumber;
        this.occupied = false;
        this.vehiclePlate = null;
    }

    public boolean occupy(String vehiclePlate) {
        if (occupied) return false;
        this.occupied = true;
        this.vehiclePlate = vehiclePlate;
        return true;
    }

    public void release() {
        this.occupied = false;
        this.vehiclePlate = null;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getLotId() {
        return lotId;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public String getSpaceNumber() {
        return spaceNumber;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public void setSpaceNumber(String spaceNumber) {
        this.spaceNumber = spaceNumber;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }
}
