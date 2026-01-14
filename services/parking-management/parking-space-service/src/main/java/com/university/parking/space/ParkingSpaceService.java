package com.university.parking.space.service;

import com.university.parking.space.model.ParkingSpace;
import com.university.parking.space.repository.ParkingSpaceRepository;
import org.springframework.stereotype.Service;

@Service
public class ParkingSpaceService {

    private final ParkingSpaceRepository repository;

    public ParkingSpaceService(ParkingSpaceRepository repository) {
        this.repository = repository;
    }

    /**
     * Asigna un espacio disponible en un lote
     */
    public ParkingSpace assignSpace(String lotId) {
        ParkingSpace space = repository.findAvailableByLot(lotId)
                .orElseThrow(() -> new RuntimeException("No available spaces"));

        if (!space.occupy()) {
            throw new RuntimeException("Space already occupied");
        }

        return repository.save(space);
    }

    /**
     * Libera un espacio de parqueo
     */
    public void releaseSpace(String spaceId) {
        ParkingSpace space = repository.findById(spaceId)
                .orElseThrow(() -> new RuntimeException("Space not found"));

        space.release();
        repository.save(space);
    }

    public void markOccupied(String spaceId) {
        ParkingSpace space = repository.findById(spaceId)
            .orElseThrow(() -> new RuntimeException("Space not found"));

        if (!space.isOccupied()) {
            space.occupy();
            repository.save(space);
        }
    }

}



