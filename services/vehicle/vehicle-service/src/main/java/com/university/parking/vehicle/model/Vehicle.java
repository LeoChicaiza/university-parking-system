package com.university.parking.vehicle.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "vehicles", schema = "core_domain")
public class Vehicle {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String plate;

    private String brand;
    private String model;

    @Column(nullable = false)
    private String ownerEmail;

    protected Vehicle() {}

    public Vehicle(String plate, String brand, String model, String ownerEmail) {
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.ownerEmail = ownerEmail;
    }

    public UUID getId() { return id; }
    public String getPlate() { return plate; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public String getOwnerEmail() { return ownerEmail; }
}
