
package com.university.parking.reporting.model;

public class UsageReport {

    private int totalEntries;
    private int totalExits;
    private double totalRevenue;

    public UsageReport(int totalEntries, int totalExits, double totalRevenue) {
        this.totalEntries = totalEntries;
        this.totalExits = totalExits;
        this.totalRevenue = totalRevenue;
    }

    public int getTotalEntries() {
        return totalEntries;
    }

    public int getTotalExits() {
        return totalExits;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }
}
