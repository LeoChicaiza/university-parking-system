package com.university.parking.space.repository;

import com.university.parking.space.model.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, UUID> {

    Optional<ParkingSpace> findFirstByLotIdAndOccupiedFalse(String lotId);
    Optional<ParkingSpace> findByLotIdAndSpaceNumber(String lotId, String spaceNumber);
    Optional<ParkingSpace> findByVehiclePlate(String vehiclePlate);
    List<ParkingSpace> findByLotId(String lotId);
    
    default Optional<ParkingSpace> findById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return findById(uuid);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
    
    @Query("SELECT COUNT(ps) FROM ParkingSpace ps WHERE ps.lotId = :lotId AND ps.occupied = false")
    Long countAvailableSpacesByLotId(@Param("lotId") String lotId);
    
    @Query("SELECT COUNT(ps) FROM ParkingSpace ps WHERE ps.lotId = :lotId AND ps.occupied = true")
    Long countOccupiedSpacesByLotId(@Param("lotId") String lotId);
}