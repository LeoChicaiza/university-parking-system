package com.university.parking.vehicle.model;

public class Vehicle {

    private String plate;
    private String brand;
    private String model;
    private String ownerEmail;

    public Vehicle(String plate, String brand, String model, String ownerEmail) {
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.ownerEmail = ownerEmail;
    }

    public String getPlate() {
        return plate;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }
}
