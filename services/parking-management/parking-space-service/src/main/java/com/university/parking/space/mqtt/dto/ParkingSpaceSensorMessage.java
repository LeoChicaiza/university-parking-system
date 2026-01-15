package com.university.parking.space.mqtt.dto;

public class ParkingSpaceSensorMessage {

    private String spaceId;
    private boolean occupied;

    public ParkingSpaceSensorMessage() {}

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
