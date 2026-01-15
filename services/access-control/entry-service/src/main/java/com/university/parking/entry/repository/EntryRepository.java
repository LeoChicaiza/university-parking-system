package com.university.parking.entry.repository;

import com.university.parking.entry.model.ParkingEntry;
import com.university.parking.entry.model.EntryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EntryRepository extends JpaRepository<ParkingEntry, UUID> {

    Optional<ParkingEntry> findByPlateAndStatus(String plate, EntryStatus status);
}


