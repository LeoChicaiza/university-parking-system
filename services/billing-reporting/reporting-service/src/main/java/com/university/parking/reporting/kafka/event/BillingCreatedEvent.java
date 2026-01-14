package com.university.parking.reporting.kafka.event;

public class BillingCreatedEvent {

    private String plate;
    private double amount;

    public BillingCreatedEvent() {}

    public BillingCreatedEvent(String plate, double amount) {
        this.plate = plate;
        this.amount = amount;
    }

    public String getPlate() {
        return plate;
    }

    public double getAmount() {
        return amount;
    }
}


