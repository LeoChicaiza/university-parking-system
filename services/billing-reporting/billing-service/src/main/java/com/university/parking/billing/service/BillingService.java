
package com.university.parking.billing.service;

import com.university.parking.billing.model.BillingResponse;
import org.springframework.stereotype.Service;

@Service
public class BillingService {

    private static final double RATE_PER_MINUTE = 0.05;

    public BillingResponse calculate(int minutes) {
        double total = minutes * RATE_PER_MINUTE;
        return new BillingResponse(total, "PENDING");
    }
}
