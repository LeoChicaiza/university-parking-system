package com.university.parking.exit.controller;

import com.university.parking.exit.model.ExitRequest;
import com.university.parking.exit.service.ExitService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/exit")
public class ExitController {

    private final ExitService service;

    public ExitController(ExitService service) {
        this.service = service;
    }

    @PostMapping
    public Map<String, Object> exit(@RequestBody ExitRequest request) {
        return service.processExit(request.plate);
    }
}

