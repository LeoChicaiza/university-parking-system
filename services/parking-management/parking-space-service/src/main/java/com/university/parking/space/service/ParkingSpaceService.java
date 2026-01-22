package com.university.parking.space.service;

import com.university.parking.space.model.ParkingSpace;
import com.university.parking.space.repository.ParkingSpaceRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ParkingSpaceService {

    private final ParkingSpaceRepository repository;

    public ParkingSpaceService(ParkingSpaceRepository repository) {
        this.repository = repository;
    }

    public ParkingSpace assignSpace(String lotId) {
        ParkingSpace space = repository.findFirstByLotIdAndOccupiedFalse(lotId)
                .orElseThrow(() -> new RuntimeException("No available spaces"));

        if (!space.occupy("TEMP_PLATE")) {
            throw new RuntimeException("Space already occupied");
        }

        return repository.save(space);
    }

    public ParkingSpace assignSpaceWithPlate(String lotId, String vehiclePlate) {
        ParkingSpace space = repository.findFirstByLotIdAndOccupiedFalse(lotId)
                .orElseThrow(() -> new RuntimeException("No available spaces"));

        if (!space.occupy(vehiclePlate)) {
            throw new RuntimeException("Space already occupied");
        }

        return repository.save(space);
    }

    public void releaseSpace(String spaceId) {
        ParkingSpace space = repository.findById(spaceId)
                .orElseThrow(() -> new RuntimeException("Space not found: " + spaceId));

        space.release();
        repository.save(space);
    }

    public void releaseSpaceByNumber(String lotId, String spaceNumber) {
        ParkingSpace space = repository.findByLotIdAndSpaceNumber(lotId, spaceNumber)
                .orElseThrow(() -> new RuntimeException("Space not found"));

        space.release();
        repository.save(space);
    }

    public void markOccupied(String spaceId) {
        ParkingSpace space = repository.findById(spaceId)
            .orElseThrow(() -> new RuntimeException("Space not found"));

        if (!space.isOccupied()) {
            space.occupy("UNKNOWN_PLATE");
            repository.save(space);
        }
    }

    public void markOccupiedWithPlate(String spaceId, String vehiclePlate) {
        ParkingSpace space = repository.findById(spaceId)
            .orElseThrow(() -> new RuntimeException("Space not found"));

        if (!space.isOccupied()) {
            space.occupy(vehiclePlate);
            repository.save(space);
        }
    }

    public ParkingSpace getSpaceById(String spaceId) {
        return repository.findById(spaceId)
                .orElseThrow(() -> new RuntimeException("Space not found"));
    }

    public ParkingSpace getSpaceByNumber(String lotId, String spaceNumber) {
        return repository.findByLotIdAndSpaceNumber(lotId, spaceNumber)
                .orElseThrow(() -> new RuntimeException("Space not found"));
    }

    public ParkingSpace findSpaceByVehiclePlate(String vehiclePlate) {
        return repository.findByVehiclePlate(vehiclePlate)
                .orElseThrow(() -> new RuntimeException("No space found for vehicle"));
    }

    public java.util.List<ParkingSpace> getSpacesByLot(String lotId) {
        return repository.findByLotId(lotId);
    }

    public Long countAvailableSpaces(String lotId) {
        return repository.countAvailableSpacesByLotId(lotId);
    }

    public Long countOccupiedSpaces(String lotId) {
        return repository.countOccupiedSpacesByLotId(lotId);
    }
}