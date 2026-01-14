package com.university.parking.vehicle.controller;

import com.university.parking.vehicle.model.Vehicle;
import com.university.parking.vehicle.service.VehicleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @PostMapping
    public Vehicle register(@RequestBody Vehicle vehicle) {
        return service.registerVehicle(vehicle);
    }

    @GetMapping("/owner/{email}")
    public List<Vehicle> getByOwner(@PathVariable String email) {
        return service.getVehiclesByOwner(email);
    }

    @GetMapping("/validate")
    public boolean validateOwnership(
            @RequestParam String plate,
            @RequestParam String email) {
        return service.validateOwnership(plate, email);
    }
}
