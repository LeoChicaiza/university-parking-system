package com.university.parking.entry.kafka.event;

public class VehicleEntryEvent {

    private String plate;
    private String entryTime;

    public VehicleEntryEvent() {}

    public VehicleEntryEvent(String plate, String entryTime) {
        this.plate = plate;
        this.entryTime = entryTime;
    }

    public String getPlate() {
        return plate;
    }

    public String getEntryTime() {
        return entryTime;
    }
}
