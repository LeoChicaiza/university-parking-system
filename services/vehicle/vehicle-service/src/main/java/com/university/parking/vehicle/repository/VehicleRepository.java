package com.university.parking.vehicle.repository;

import com.university.parking.vehicle.model.Vehicle;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class VehicleRepository {

    private final Map<String, Vehicle> vehicles = new HashMap<>();

    public Vehicle save(Vehicle vehicle) {
        vehicles.put(vehicle.getPlate(), vehicle);
        return vehicle;
    }

    public Optional<Vehicle> findByPlate(String plate) {
        return Optional.ofNullable(vehicles.get(plate));
    }

    public List<Vehicle> findByOwner(String email) {
        return vehicles.values()
                .stream()
                .filter(v -> v.getOwnerEmail().equals(email))
                .toList();
    }
}
