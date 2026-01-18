package com.university.parking.space.repository;

import com.university.parking.space.model.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, String> {

    // Método CORREGIDO: findFirstByLotIdAndOccupiedFalse
    Optional<ParkingSpace> findFirstByLotIdAndOccupiedFalse(String lotId);
}