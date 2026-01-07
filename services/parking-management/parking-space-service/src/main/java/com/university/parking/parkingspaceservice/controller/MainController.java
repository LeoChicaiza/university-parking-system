
package com.university.parking.parkingspaceservice.controller;

import org.springframework.web.bind.annotation.*;
import com.university.parking.parkingspaceservice.service.MainService;

@RestController
@RequestMapping("/api/parking-space-service")
public class MainController {
    private final MainService service;

    public MainController(MainService service) {
        this.service = service;
    }

    @PostMapping
    public String process() {
        return service.execute();
    }
}
