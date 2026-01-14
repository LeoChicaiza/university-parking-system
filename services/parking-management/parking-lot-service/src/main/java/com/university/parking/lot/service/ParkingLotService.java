package com.university.parking.lot.service;

import com.university.parking.lot.model.ParkingLot;
import com.university.parking.lot.repository.ParkingLotRepository;
import org.springframework.stereotype.Service;

@Service
public class ParkingLotService {

    private final ParkingLotRepository repository;

    public ParkingLotService(ParkingLotRepository repository) {
        this.repository = repository;
    }

    public void occupy(String lotId) {
        ParkingLot lot = repository.findById(lotId)
                .orElseThrow(() -> new RuntimeException("Lot not found"));

        if (!lot.occupy()) {
            throw new RuntimeException("Parking lot full");
        }
    }

    public void release(String lotId) {
        ParkingLot lot = repository.findById(lotId)
                .orElseThrow(() -> new RuntimeException("Lot not found"));
        lot.release();
    }
}

