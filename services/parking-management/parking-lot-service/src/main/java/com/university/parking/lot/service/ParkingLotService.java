package com.university.parking.lot.service;

import com.university.parking.lot.model.ParkingLot;
import com.university.parking.lot.repository.ParkingLotRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ParkingLotService {

    private static final String CAPACITY_KEY = "parking-lot:%s:capacity";
    private static final String OCCUPIED_KEY = "parking-lot:%s:occupied";

    private final ParkingLotRepository repository;
    private final StringRedisTemplate redisTemplate;

    public ParkingLotService(ParkingLotRepository repository, StringRedisTemplate redisTemplate) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
    }

    public void occupy(String lotId) {
        String capacityKey = CAPACITY_KEY.formatted(lotId);
        String occupiedKey = OCCUPIED_KEY.formatted(lotId);

        String capacityStr = redisTemplate.opsForValue().get(capacityKey);
        String occupiedStr = redisTemplate.opsForValue().get(occupiedKey);
        
        if (capacityStr == null) {
            throw new RuntimeException("Parking lot capacity not set in Redis");
        }
        
        int capacity = Integer.parseInt(capacityStr);
        int occupied = (occupiedStr != null) ? Integer.parseInt(occupiedStr) : 0;

        if (occupied >= capacity) {
            throw new RuntimeException("Parking lot full");
        }

        redisTemplate.opsForValue().increment(occupiedKey);

        ParkingLot lot = repository.findById(lotId)
                .orElseThrow(() -> new RuntimeException("Lot not found: " + lotId));

        lot.occupy();
        repository.save(lot);
    }

    public void occupy(UUID lotId) {
        occupy(lotId.toString());
    }

    public void release(String lotId) {
        String occupiedKey = OCCUPIED_KEY.formatted(lotId);

        Long newValue = redisTemplate.opsForValue().decrement(occupiedKey);
        
        if (newValue != null && newValue < 0) {
            redisTemplate.opsForValue().set(occupiedKey, "0");
        }

        ParkingLot lot = repository.findById(lotId)
                .orElseThrow(() -> new RuntimeException("Lot not found: " + lotId));

        lot.release();
        repository.save(lot);
    }

    public void release(UUID lotId) {
        release(lotId.toString());
    }

    public ParkingLot createParkingLot(String name, int capacity) {
        ParkingLot lot = new ParkingLot(name, capacity);
        ParkingLot savedLot = repository.save(lot);
        
        String lotId = savedLot.getId().toString();
        String capacityKey = CAPACITY_KEY.formatted(lotId);
        String occupiedKey = OCCUPIED_KEY.formatted(lotId);
        
        redisTemplate.opsForValue().set(capacityKey, String.valueOf(capacity));
        redisTemplate.opsForValue().set(occupiedKey, "0");
        
        return savedLot;
    }

    public ParkingLot getParkingLot(String lotId) {
        return repository.findById(lotId)
                .orElseThrow(() -> new RuntimeException("Lot not found: " + lotId));
    }

    public ParkingLot getParkingLot(UUID lotId) {
        return repository.findById(lotId)
                .orElseThrow(() -> new RuntimeException("Lot not found: " + lotId));
    }

    public ParkingLot getParkingLotByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Lot not found with name: " + name));
    }

    public int getAvailableSpaces(String lotId) {
        String capacityKey = CAPACITY_KEY.formatted(lotId);
        String occupiedKey = OCCUPIED_KEY.formatted(lotId);
        
        String capacityStr = redisTemplate.opsForValue().get(capacityKey);
        String occupiedStr = redisTemplate.opsForValue().get(occupiedKey);
        
        if (capacityStr == null || occupiedStr == null) {
            return 0;
        }
        
        int capacity = Integer.parseInt(capacityStr);
        int occupied = Integer.parseInt(occupiedStr);
        
        return Math.max(0, capacity - occupied);
    }

    public int getCurrentOccupancy(String lotId) {
        String occupiedKey = OCCUPIED_KEY.formatted(lotId);
        String occupiedStr = redisTemplate.opsForValue().get(occupiedKey);
        
        return (occupiedStr != null) ? Integer.parseInt(occupiedStr) : 0;
    }

    public int getTotalCapacity(String lotId) {
        String capacityKey = CAPACITY_KEY.formatted(lotId);
        String capacityStr = redisTemplate.opsForValue().get(capacityKey);
        
        return (capacityStr != null) ? Integer.parseInt(capacityStr) : 0;
    }

    public void updateCapacity(String lotId, int newCapacity) {
        if (newCapacity < 0) {
            throw new RuntimeException("Capacity cannot be negative");
        }
        
        String capacityKey = CAPACITY_KEY.formatted(lotId);
        redisTemplate.opsForValue().set(capacityKey, String.valueOf(newCapacity));
        
        repository.findById(lotId).ifPresent(lot -> {
            lot.setCapacity(newCapacity);
            repository.save(lot);
        });
    }
}