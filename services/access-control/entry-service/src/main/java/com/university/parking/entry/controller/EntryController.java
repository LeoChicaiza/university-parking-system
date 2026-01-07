
package com.university.parking.entry.controller;

import com.university.parking.entry.model.EntryRequest;
import com.university.parking.entry.model.EntryResponse;
import com.university.parking.entry.service.EntryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entry")
public class EntryController {

    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @PostMapping
    public EntryResponse registerEntry(@RequestBody EntryRequest request) {
        return entryService.processEntry(request);
    }
}
