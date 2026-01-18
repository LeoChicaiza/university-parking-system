package com.university.parking.exit.controller;

import com.university.parking.exit.model.ExitRequest;
import com.university.parking.exit.service.ExitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/exit")
public class ExitController {

    private final ExitService service;

    public ExitController(ExitService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> exit(@RequestBody ExitRequest request) {
        try {
            if (request.plate == null || request.plate.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Plate is required"));
            }
            
            Map<String, Object> result = service.processExit(request.plate);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}