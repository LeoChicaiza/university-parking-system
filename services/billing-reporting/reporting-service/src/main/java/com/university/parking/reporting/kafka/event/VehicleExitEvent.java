package com.university.parking.reporting.kafka.event;

import java.time.LocalDateTime;

public class VehicleExitEvent {
    private String plate;
    private String parkingZone;
    private LocalDateTime exitTime;
    
    // Constructor vacío
    public VehicleExitEvent() {}
    
    // Constructor con parámetros
    public VehicleExitEvent(String plate, String parkingZone, LocalDateTime exitTime) {
        this.plate = plate;
        this.parkingZone = parkingZone;
        this.exitTime = exitTime;
    }
    
    // Getters y setters
    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }
    
    public String getParkingZone() { return parkingZone; }
    public void setParkingZone(String parkingZone) { this.parkingZone = parkingZone; }
    
    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
}