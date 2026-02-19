package com.university.parking.space.controller;

import com.university.parking.space.model.ParkingSpace;
import com.university.parking.space.service.ParkingSpaceService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ParkingSpace> assign(@RequestBody Map<String, String> body) {
        try {
            String lotId = body.get("lotId");
            String vehiclePlate = body.get("vehiclePlate");
            
            ParkingSpace space;
            if (vehiclePlate != null && !vehiclePlate.isEmpty()) {
                space = service.assignSpaceWithPlate(lotId, vehiclePlate);
            } else {
                space = service.assignSpace(lotId);
            }
            
            return ResponseEntity.ok(space);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<Void> release(@PathVariable String id) {
        try {
            service.releaseSpace(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/release-by-number")
    public ResponseEntity<Void> releaseByNumber(@RequestBody Map<String, String> body) {
        try {
            String lotId = body.get("lotId");
            String spaceNumber = body.get("spaceNumber");
            service.releaseSpaceByNumber(lotId, spaceNumber);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpace> getSpace(@PathVariable String id) {
        try {
            ParkingSpace space = service.getSpaceById(id);
            return ResponseEntity.ok(space);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-number")
    public ResponseEntity<ParkingSpace> getSpaceByNumber(
            @RequestParam String lotId, 
            @RequestParam String spaceNumber) {
        try {
            ParkingSpace space = service.getSpaceByNumber(lotId, spaceNumber);
            return ResponseEntity.ok(space);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-vehicle/{vehiclePlate}")
    public ResponseEntity<ParkingSpace> getSpaceByVehicle(@PathVariable String vehiclePlate) {
        try {
            ParkingSpace space = service.findSpaceByVehiclePlate(vehiclePlate);
            return ResponseEntity.ok(space);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/lot/{lotId}")
    public ResponseEntity<java.util.List<ParkingSpace>> getSpacesByLot(@PathVariable String lotId) {
        java.util.List<ParkingSpace> spaces = service.getSpacesByLot(lotId);
        return ResponseEntity.ok(spaces);
    }

    @GetMapping("/lot/{lotId}/available-count")
    public ResponseEntity<Long> countAvailableSpaces(@PathVariable String lotId) {
        Long count = service.countAvailableSpaces(lotId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/lot/{lotId}/occupied-count")
    public ResponseEntity<Long> countOccupiedSpaces(@PathVariable String lotId) {
        Long count = service.countOccupiedSpaces(lotId);
        return ResponseEntity.ok(count);
    }
}
