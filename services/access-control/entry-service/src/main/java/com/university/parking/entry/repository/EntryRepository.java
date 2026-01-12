package com.university.parking.entry.repository;

import com.university.parking.entry.model.EntryRecord;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class EntryRepository {

    private final Map<String, EntryRecord> records = new HashMap<>();

    public EntryRecord save(EntryRecord record) {
        records.put(record.getEntryId(), record);
        return record;
    }

    public Optional<EntryRecord> findActiveByPlate(String plate) {
        return records.values().stream()
                .filter(r -> r.getPlate().equals(plate))
                .filter(EntryRecord::isActive)
                .findFirst();
    }

    public Optional<EntryRecord> findById(String entryId) {
        return Optional.ofNullable(records.get(entryId));
    }
}

