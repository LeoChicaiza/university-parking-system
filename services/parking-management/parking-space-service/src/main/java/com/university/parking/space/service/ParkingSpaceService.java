package com.university.parking.space.service;

import com.university.parking.space.model.ParkingSpace;
import com.university.parking.space.repository.ParkingSpaceRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ParkingSpaceService {

    private final ParkingSpaceRepository repository;
    private final RedisTemplate<String, Object> redisTemplate;

    public ParkingSpaceService(
            ParkingSpaceRepository repository,
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
    }

    public ParkingSpace assignSpace(String lotId) {

        String redisKey = "available-spaces:" + lotId;

        // 1️⃣ Consultar Redis
        Integer available = (Integer) redisTemplate.opsForValue().get(redisKey);

        if (available != null && available <= 0) {
            throw new RuntimeException("No available spaces (cached)");
        }

        // 2️⃣ Consultar base de datos
        ParkingSpace space = repository.findAvailableByLot(lotId)
                .orElseThrow(() -> new RuntimeException("No available spaces"));

        // 3️⃣ Ocupar espacio
        space.occupy();
        repository.save(space);

        // 4️⃣ Actualizar Redis
        if (available == null) {
            redisTemplate.opsForValue().set(redisKey, space.getAvailableCount() - 1);
        } else {
            redisTemplate.opsForValue().set(redisKey, available - 1);
        }

        return space;
    }

    public void releaseSpace(String spaceId) {

        ParkingSpace space = repository.findById(spaceId)
                .orElseThrow(() -> new RuntimeException("Space not found"));

        space.release();
        repository.save(space);

        String redisKey = "available-spaces:" + space.getLotId();

        Integer available = (Integer) redisTemplate.opsForValue().get(redisKey);
        if (available != null) {
            redisTemplate.opsForValue().set(redisKey, available + 1);
        }
    }
}

