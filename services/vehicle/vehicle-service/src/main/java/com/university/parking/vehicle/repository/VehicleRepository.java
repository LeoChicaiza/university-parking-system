package com.university.parking.vehicle.repository;

import com.university.parking.vehicle.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    Optional<Vehicle> findByPlate(String plate);
    List<Vehicle> findByOwner(String owner);  // Cambiado a findByOwner
}
