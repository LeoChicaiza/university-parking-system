package com.university.parking.space.repository;

import com.university.parking.space.model.ParkingSpace;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ParkingSpaceRepository {

    private final Map<String, ParkingSpace> spaces = new HashMap<>();

    public ParkingSpaceRepository() {
        for (int i = 1; i <= 50; i++) {
            spaces.put("S" + i, new ParkingSpace("S" + i, "LOT-1"));
        }
    }

    public Optional<ParkingSpace> findAvailableByLot(String lotId) {
        return spaces.values().stream()
                .filter(s -> s.getLotId().equals(lotId))
                .filter(s -> !s.isOccupied())
                .findFirst();
    }

    public Optional<ParkingSpace> findById(String id) {
        return Optional.ofNullable(spaces.get(id));
    }
}
