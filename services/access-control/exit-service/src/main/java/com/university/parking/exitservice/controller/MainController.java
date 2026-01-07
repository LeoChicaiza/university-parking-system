
package com.university.parking.exitservice.controller;

import org.springframework.web.bind.annotation.*;
import com.university.parking.exitservice.service.MainService;

@RestController
@RequestMapping("/api/exit-service")
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
