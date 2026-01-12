package com.university.parking.billing.model;

public class BillingRecord {

    private String entryId;
    private String plate;
    private long durationMinutes;
    private double amount;

    public BillingRecord(String entryId, String plate,
                         long durationMinutes, double amount) {
        this.entryId = entryId;
        this.plate = plate;
        this.durationMinutes = durationMinutes;
        this.amount = amount;
    }

    public String getEntryId() {
        return entryId;
    }

    public String getPlate() {
        return plate;
    }

    public long getDurationMinutes() {
        return durationMinutes;
    }

    public double getAmount() {
        return amount;
    }
}

