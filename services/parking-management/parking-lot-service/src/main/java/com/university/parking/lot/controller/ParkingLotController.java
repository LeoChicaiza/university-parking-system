package com.university.parking.lot.controller;

import com.university.parking.lot.service.ParkingLotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
