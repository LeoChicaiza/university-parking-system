package com.university.parking.billing.repository;

import com.university.parking.billing.model.BillingRecord;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BillingRepository {

    private final Map<String, BillingRecord> records = new HashMap<>();

    public BillingRecord save(BillingRecord record) {
        records.put(record.getEntryId(), record);
        return record;
    }

    public Optional<BillingRecord> findByEntryId(String entryId) {
        return Optional.ofNullable(records.get(entryId));
    }

    public Collection<BillingRecord> findAll() {
        return records.values();
    }
}
