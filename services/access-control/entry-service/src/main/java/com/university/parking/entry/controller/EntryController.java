package com.university.parking.entry.controller;

import com.university.parking.entry.model.EntryRecord;
import com.university.parking.entry.model.EntryRequest;
import com.university.parking.entry.service.EntryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entry")
public class EntryController {

    private final EntryService service;

    public EntryController(EntryService service) {
        this.service = service;
    }

    @PostMapping
    public EntryRecord entry(@RequestBody EntryRequest request) {
        return service.processEntry(request);
    }

    @GetMapping("/active/{plate}")
    public EntryRecord active(@PathVariable String plate) {
        return service.getActiveEntry(plate);
    }
}
