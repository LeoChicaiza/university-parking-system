package com.university.parking.lot.service;

import com.university.parking.lot.model.ParkingLot;
import com.university.parking.lot.repository.ParkingLotRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ParkingLotService {

    private static final String CAPACITY_KEY = "parking-lot:%s:capacity";
    private static final String OCCUPIED_KEY = "parking-lot:%s:occupied";

    private final ParkingLotRepository repository;
    private final StringRedisTemplate redisTemplate;

    public ParkingLotService(
            ParkingLotRepository repository,
            StringRedisTemplate redisTemplate
    ) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
    }

    public void occupy(String lotId) {
        String capacityKey = CAPACITY_KEY.formatted(lotId);
        String occupiedKey = OCCUPIED_KEY.formatted(lotId);

        // 1️⃣ Obtener valores desde Redis
        String capacityStr = redisTemplate.opsForValue().get(capacityKey);
        String occupiedStr = redisTemplate.opsForValue().get(occupiedKey);
        
        if (capacityStr == null) {
            throw new RuntimeException("Parking lot capacity not set in Redis");
        }
        
        int capacity = Integer.parseInt(capacityStr);
        int occupied = (occupiedStr != null) ? Integer.parseInt(occupiedStr) : 0;

        // 2️⃣ Validar disponibilidad
        if (occupied >= capacity) {
            throw new RuntimeException("Parking lot full");
        }

        // 3️⃣ Incrementar ocupación
        redisTemplate.opsForValue().increment(occupiedKey);

        // 4️⃣ (Opcional) Persistir estado en BD
        ParkingLot lot = repository.findById(lotId)
                .orElseThrow(() -> new RuntimeException("Lot not found"));

        lot.occupy();
        repository.save(lot);
    }

    public void release(String lotId) {
        String occupiedKey = OCCUPIED_KEY.formatted(lotId);

        // 1️⃣ Decrementar ocupación Redis
        Long newValue = redisTemplate.opsForValue().decrement(occupiedKey);
        
        // Si el valor es negativo, establecer a 0
        if (newValue != null && newValue < 0) {
            redisTemplate.opsForValue().set(occupiedKey, "0");
        }

        // 2️⃣ (Opcional) Persistir en BD
        ParkingLot lot = repository.findById(lotId)
                .orElseThrow(() -> new RuntimeException("Lot not found"));

        lot.release();
        repository.save(lot);
    }
}