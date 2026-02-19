package com.university.parking.lot.repository;

import com.university.parking.lot.model.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, UUID> {
    Optional<ParkingLot> findByName(String name);
    
    default Optional<ParkingLot> findById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return findById(uuid);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
    
    @Query("SELECT SUM(p.capacity) FROM ParkingLot p")
    Long getTotalCapacity();
    
    @Query("SELECT SUM(p.occupied) FROM ParkingLot p")
    Long getTotalOccupied();
    
    @Query("SELECT p FROM ParkingLot p WHERE p.occupied < p.capacity")
    List<ParkingLot> findAvailableLots();
    
    @Query("SELECT COUNT(p) FROM ParkingLot p WHERE p.occupied >= p.capacity")
    Long countFullLots();
}