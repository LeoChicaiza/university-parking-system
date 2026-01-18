package com.university.parking.space.kafka.event;

import java.time.LocalDateTime;

public class VehicleEntryEvent {
    private String plate;
    private String parkingZone;
    private LocalDateTime entryTime;
    
    // Getters y setters
    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }
    
    public String getParkingZone() { return parkingZone; }
    public void setParkingZone(String parkingZone) { this.parkingZone = parkingZone; }
    
    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }
}