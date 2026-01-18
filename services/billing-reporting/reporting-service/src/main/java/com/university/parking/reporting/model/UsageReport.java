package com.university.parking.reporting.model;

public class UsageReport {
    private long totalEntries;
    private long totalExits;
    private double totalRevenue;
    private long activeVehicles;
    
    // Constructor vacío (necesario para Jackson/JSON)
    public UsageReport() {}
    
    // Constructor con parámetros
    public UsageReport(long totalEntries, long totalExits, double totalRevenue) {
        this.totalEntries = totalEntries;
        this.totalExits = totalExits;
        this.totalRevenue = totalRevenue;
        this.activeVehicles = Math.max(0, totalEntries - totalExits); // Evitar valores negativos
    }
    
    // Getters y setters
    public long getTotalEntries() {
        return totalEntries;
    }
    
    public void setTotalEntries(long totalEntries) {
        this.totalEntries = totalEntries;
    }
    
    public long getTotalExits() {
        return totalExits;
    }
    
    public void setTotalExits(long totalExits) {
        this.totalExits = totalExits;
    }
    
    public double getTotalRevenue() {
        return totalRevenue;
    }
    
    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    
    public long getActiveVehicles() {
        return activeVehicles;
    }
    
    public void setActiveVehicles(long activeVehicles) {
        this.activeVehicles = activeVehicles;
    }
    
    @Override
    public String toString() {
        return "UsageReport{" +
                "totalEntries=" + totalEntries +
                ", totalExits=" + totalExits +
                ", totalRevenue=" + totalRevenue +
                ", activeVehicles=" + activeVehicles +
                '}';
    }
}