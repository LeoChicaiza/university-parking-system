package com.university.parking.billing.kafka.event;

public class VehicleExitEvent {

    private String plate;
    private long exitTimeMillis;

    public VehicleExitEvent() {}

    public VehicleExitEvent(String plate, long exitTimeMillis) {
        this.plate = plate;
        this.exitTimeMillis = exitTimeMillis;
    }

    public String getPlate() {
        return plate;
    }

    public long getExitTimeMillis() {
        return exitTimeMillis;
    }
}


