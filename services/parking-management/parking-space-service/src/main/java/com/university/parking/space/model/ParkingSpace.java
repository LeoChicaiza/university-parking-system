package com.university.parking.space.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "parking_spaces", schema = "core_domain")  // Agregado schema
public class ParkingSpace {

    @Id
    @GeneratedValue
    private UUID id;  // Cambiado a UUID

    @Column(name = "lot_id", nullable = false)
    private String lotId;

    @Column(nullable = false)
    private boolean occupied;

    @Column(name = "space_number", nullable = false)
    private String spaceNumber;  // Agregado para identificar el espacio

    @Column(name = "vehicle_plate")
    private String vehiclePlate;  // Agregado para saber qué vehículo ocupa

    protected ParkingSpace() {
        // JPA
    }

    public ParkingSpace(String lotId, String spaceNumber) {
        this.lotId = lotId;
        this.spaceNumber = spaceNumber;
        this.occupied = false;
        this.vehiclePlate = null;
    }

    public boolean occupy(String vehiclePlate) {
        if (occupied) {
            return false;
        }
        occupied = true;
        this.vehiclePlate = vehiclePlate;
        return true;
    }

    public void release() {
        occupied = false;
        this.vehiclePlate = null;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public String getLotId() {
        return lotId;
    }

    public String getSpaceNumber() {
        return spaceNumber;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    // Setters
    public void setId(UUID id) {
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

    // Método adicional si necesitas
    public boolean getAvailable() {
        return !occupied;
    }
}
