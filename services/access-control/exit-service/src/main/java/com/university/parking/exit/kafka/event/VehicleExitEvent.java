package com.university.parking.exit.kafka.event;

public class VehicleExitEvent {

    private String plate;
    private String exitTime;

    public VehicleExitEvent() {}

    public VehicleExitEvent(String plate, String exitTime) {
        this.plate = plate;
        this.exitTime = exitTime;
    }

    public String getPlate() {
        return plate;
    }

    public String getExitTime() {
        return exitTime;
    }
}

