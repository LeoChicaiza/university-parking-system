
package com.university.parking.entry.model;

public class EntryResponse {
    private String status;
    private String parkingSpace;
    private String timestamp;

    public EntryResponse(String status, String parkingSpace, String timestamp) {
        this.status = status;
        this.parkingSpace = parkingSpace;
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public String getParkingSpace() {
        return parkingSpace;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
