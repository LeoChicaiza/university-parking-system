package com.university.parking.lot.controller;

import com.university.parking.lot.model.ParkingLot;
import com.university.parking.lot.service.ParkingLotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/parking-lots")
public class ParkingLotController {

    private final ParkingLotService service;

    public ParkingLotController(ParkingLotService service) {
        this.service = service;
    }

    @PostMapping("/{id}/occupy")
    public ResponseEntity<String> occupy(@PathVariable String id) {
        try {
            service.occupy(id);
            return ResponseEntity.ok("Parking lot occupied successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<String> release(@PathVariable String id) {
        try {
            service.release(id);
            return ResponseEntity.ok("Parking lot released successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }

    @PostMapping
    public ResponseEntity<ParkingLot> create(@RequestBody Map<String, Object> request) {
        try {
            String name = (String) request.get("name");
            Integer capacity = (Integer) request.get("capacity");
            
            if (name == null || capacity == null) {
                return ResponseEntity.badRequest().build();
            }
            
            ParkingLot lot = service.createParkingLot(name, capacity);
            return ResponseEntity.ok(lot);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingLot> getParkingLot(@PathVariable String id) {
        try {
            ParkingLot lot = service.getParkingLot(id);
            return ResponseEntity.ok(lot);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ParkingLot> getParkingLotByName(@PathVariable String name) {
        try {
            ParkingLot lot = service.getParkingLotByName(name);
            return ResponseEntity.ok(lot);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/available-spaces")
    public ResponseEntity<Integer> getAvailableSpaces(@PathVariable String id) {
        try {
            int available = service.getAvailableSpaces(id);
            return ResponseEntity.ok(available);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/occupancy")
    public ResponseEntity<Integer> getCurrentOccupancy(@PathVariable String id) {
        try {
            int occupancy = service.getCurrentOccupancy(id);
            return ResponseEntity.ok(occupancy);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/capacity")
    public ResponseEntity<Integer> getTotalCapacity(@PathVariable String id) {
        try {
            int capacity = service.getTotalCapacity(id);
            return ResponseEntity.ok(capacity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/capacity")
    public ResponseEntity<String> updateCapacity(@PathVariable String id, 
                                                @RequestBody Map<String, Integer> request) {
        try {
            Integer newCapacity = request.get("capacity");
            if (newCapacity == null) {
                return ResponseEntity.badRequest().body("Capacity is required");
            }
            
            service.updateCapacity(id, newCapacity);
            return ResponseEntity.ok("Capacity updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }
}