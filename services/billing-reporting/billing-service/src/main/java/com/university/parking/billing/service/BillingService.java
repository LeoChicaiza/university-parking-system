package com.university.parking.billing.service;

import com.university.parking.billing.model.BillingRecord;
import com.university.parking.billing.model.BillingRequest;
import com.university.parking.billing.repository.BillingRepository;
import org.springframework.stereotype.Service;

@Service
public class BillingService {

    private static final int FREE_MINUTES = 5;
    private static final int BLOCK_MINUTES = 30;
    private static final double BLOCK_PRICE = 0.50;
    private static final double MAX_DAILY = 8.00;

    private final BillingRepository repository;

    public BillingService(BillingRepository repository) {
        this.repository = repository;
    }

    public BillingRecord calculate(BillingRequest request) {

        long durationMillis = request.exitTime - request.entryTime;
        long minutes = durationMillis / 60000;

        if (minutes <= FREE_MINUTES) {
            BillingRecord record =
                    new BillingRecord(request.entryId, request.plate, minutes, 0.0);
            return repository.save(record);
        }

        long billableMinutes = minutes - FREE_MINUTES;

        long blocks = (long) Math.ceil(
                (double) billableMinutes / BLOCK_MINUTES
        );

        double amount = blocks * BLOCK_PRICE;

        if (amount > MAX_DAILY) {
            amount = MAX_DAILY;
        }

        BillingRecord record =
                new BillingRecord(request.entryId, request.plate, minutes, amount);

        return repository.save(record);
    }
}
