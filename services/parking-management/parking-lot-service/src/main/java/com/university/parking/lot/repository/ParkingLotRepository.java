package com.university.parking.lot.repository;

import com.university.parking.lot.model.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, String> {
}
