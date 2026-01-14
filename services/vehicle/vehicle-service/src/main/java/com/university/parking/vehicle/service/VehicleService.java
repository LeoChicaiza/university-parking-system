package com.university.parking.vehicle.service;

import com.university.parking.vehicle.model.Vehicle;
import com.university.parking.vehicle.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository repository;

    public VehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    public Vehicle registerVehicle(Vehicle vehicle) {
        return repository.save(vehicle);
    }

    public boolean validateOwnership(String plate, String email) {
        return repository.findByPlate(plate)
                .map(v -> v.getOwnerEmail().equals(email))
                .orElse(false);
    }

    public List<Vehicle> getVehiclesByOwner(String email) {
        return repository.findByOwner(email);
    }
}
