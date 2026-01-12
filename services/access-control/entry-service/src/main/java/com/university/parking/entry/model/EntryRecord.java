package com.university.parking.entry.model;

public class EntryRecord {

    private String entryId;
    private String plate;
    private String spaceId;
    private String lotId;
    private String userEmail;
    private long entryTime;
    private boolean active;

    public EntryRecord(String entryId, String plate, String spaceId,
                       String lotId, String userEmail, long entryTime) {
        this.entryId = entryId;
        this.plate = plate;
        this.spaceId = spaceId;
        this.lotId = lotId;
        this.userEmail = userEmail;
        this.entryTime = entryTime;
        this.active = true;
    }

    public String getEntryId() {
        return entryId;
    }

    public String getPlate() {
        return plate;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public String getLotId() {
        return lotId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public boolean isActive() {
        return active;
    }

    public void close() {
        this.active = false;
    }
}

