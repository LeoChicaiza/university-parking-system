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
        int capacity = Integer.parseInt(
                redisTemplate.opsForValue().get(capacityKey)
        );

        int occupied = Integer.parseInt(
                redisTemplate.opsForValue().getOrDefault(occupiedKey, "0")
        );

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
        redisTemplate.opsForValue().decrement(occupiedKey);

        // 2️⃣ (Opcional) Persistir en BD
        ParkingLot lot = repository.findById(lotId)
                .orElseThrow(() -> new RuntimeException("Lot not found"));

        lot.release();
        repository.save(lot);
    }
}
