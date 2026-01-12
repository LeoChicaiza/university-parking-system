package com.university.parking.lot.repository;

import com.university.parking.lot.model.ParkingLot;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ParkingLotRepository {

    private final Map<String, ParkingLot> lots = new HashMap<>();

    public ParkingLotRepository() {
        lots.put("LOT-1", new ParkingLot("LOT-1", 50));
    }

    public Optional<ParkingLot> findById(String id) {
        return Optional.ofNullable(lots.get(id));
    }
}
