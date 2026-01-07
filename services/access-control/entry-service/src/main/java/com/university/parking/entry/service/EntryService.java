
package com.university.parking.entry.service;

import com.university.parking.entry.model.EntryRequest;
import com.university.parking.entry.model.EntryResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EntryService {

    public EntryResponse processEntry(EntryRequest request) {
        // Academic mock logic
        return new EntryResponse(
                "APPROVED",
                "A-12",
                LocalDateTime.now().toString()
        );
    }
}
