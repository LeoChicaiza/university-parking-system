
package com.university.parking.billing.model;

public class BillingResponse {
    private double amount;
    private String status;

    public BillingResponse(double amount, String status) {
        this.amount = amount;
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }
}
