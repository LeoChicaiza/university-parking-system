package com.university.parking.lot.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "parking_lots", schema = "core_domain")
public class ParkingLot {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int occupied;

    protected ParkingLot() {
        // JPA
    }

    public ParkingLot(String name, int capacity) {  // Cambiado: no recibe ID
        this.name = name;
        this.capacity = capacity;
        this.occupied = 0;
    }

    // Constructor con UUID si lo necesitas
    public ParkingLot(UUID id, String name, int capacity) {
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
    public UUID getId() {  // Cambiado: devuelve UUID, no String
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getOccupied() {
        return occupied;
    }

    // Setters si los necesitas
    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
