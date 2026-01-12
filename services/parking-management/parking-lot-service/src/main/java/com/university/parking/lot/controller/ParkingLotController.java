package com.university.parking.lot.controller;

import com.university.parking.lot.service.ParkingLotService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parking-lots")
public class ParkingLotController {

    private final ParkingLotService service;

    public ParkingLotController(ParkingLotService service) {
        this.service = service;
    }

    @PostMapping("/{id}/occupy")
    public void occupy(@PathVariable String id) {
        service.occupy(id);
    }

    @PostMapping("/{id}/release")
    public void release(@PathVariable String id) {
        service.release(id);
    }
}
