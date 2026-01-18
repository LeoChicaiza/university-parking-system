package com.university.parking.reporting.kafka.event;

import java.time.LocalDateTime;

public class BillingCreatedEvent {
    private String billingId;
    private String plate;
    private double amount;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private String parkingZone;
    
    // Constructor vacío
    public BillingCreatedEvent() {}
    
    // Constructor con parámetros
    public BillingCreatedEvent(String billingId, String plate, double amount, 
                              LocalDateTime entryTime, LocalDateTime exitTime, String parkingZone) {
        this.billingId = billingId;
        this.plate = plate;
        this.amount = amount;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.parkingZone = parkingZone;
    }
    
    // Getters y setters
    public String getBillingId() { return billingId; }
    public void setBillingId(String billingId) { this.billingId = billingId; }
    
    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }
    
    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    
    public String getParkingZone() { return parkingZone; }
    public void setParkingZone(String parkingZone) { this.parkingZone = parkingZone; }
}