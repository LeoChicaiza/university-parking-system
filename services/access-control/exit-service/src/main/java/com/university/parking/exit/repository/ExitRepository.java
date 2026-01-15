package com.university.parking.exit.repository;

import com.university.parking.exit.model.ParkingExit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExitRepository extends JpaRepository<ParkingExit, UUID> {
}
