package com.university.parking.space.controller;

import com.university.parking.space.model.ParkingSpace;
import com.university.parking.space.service.ParkingSpaceService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/parking-spaces")
public class ParkingSpaceController {

    private final ParkingSpaceService service;

    public ParkingSpaceController(ParkingSpaceService service) {
        this.service = service;
    }

    @PostMapping("/assign")
    public ParkingSpace assign(@RequestBody Map<String, String> body) {
        return service.assignSpace(body.get("lotId"));
    }

    @PostMapping("/{id}/release")
    public void release(@PathVariable String id) {
        service.releaseSpace(id);
    }
}

