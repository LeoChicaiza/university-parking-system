package com.university.parking.entry.service;

import com.university.parking.entry.kafka.event.VehicleEntryEvent;
import com.university.parking.entry.kafka.producer.EntryEventProducer;
import com.university.parking.entry.model.*;
import com.university.parking.entry.repository.EntryRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class EntryService {

    private static final String ACTIVE_ENTRY_KEY = "active-entry:";

    private final EntryRepository repository;
    private final RestTemplate restTemplate;
    private final EntryEventProducer producer;
    private final StringRedisTemplate redisTemplate;

    public EntryService(
            EntryRepository repository,
            RestTemplate restTemplate,
            EntryEventProducer producer,
            StringRedisTemplate redisTemplate
    ) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.producer = producer;
        this.redisTemplate = redisTemplate;
    }

    public ParkingEntry processEntry(EntryRequest request) {

        String redisKey = ACTIVE_ENTRY_KEY + request.plate;

        // 1️⃣ VALIDAR EN REDIS (RÁPIDO)
        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            throw new RuntimeException("Vehicle already inside (Redis)");
        }

        // 2️⃣ VALIDAR EN BD (CONSISTENCIA)
        repository.findByPlateAndStatus(request.plate, EntryStatus.ACTIVE)
                .ifPresent(r -> {
                    throw new RuntimeException("Vehicle already inside (DB)");
                });

        // 3️⃣ VALIDAR VEHÍCULO
        Boolean validVehicle = restTemplate.getForObject(
                "http://vehicle-service/vehicles/validate?plate={plate}&email={email}",
                Boolean.class,
                request.plate,
                request.userEmail
        );

        if (Boolean.FALSE.equals(validVehicle)) {
            throw new RuntimeException("Vehicle not authorized");
        }

        // 4️⃣ ASIGNAR ESPACIO
        Map space = restTemplate.postForObject(
                "http://parking-space-service/parking-spaces/assign",
                Map.of("lotId", request.lotId),
                Map.class
        );

        String spaceId = (String) space.get("id");

        // 5️⃣ OCUPAR CAPACIDAD
        restTemplate.postForObject(
                "http://parking-lot-service/parking-lots/{id}/occupy",
                null,
                Void.class,
                request.lotId
        );

        // 6️⃣ PERSISTIR ENTRADA
        ParkingEntry entry = new ParkingEntry(
                request.plate,
                spaceId,
                request.lotId,
                request.userEmail,
                System.currentTimeMillis()
        );

        ParkingEntry saved = repository.save(entry);

        // 7️⃣ GUARDAR EN REDIS
        redisTemplate.opsForValue().set(
                redisKey,
                saved.getId().toString(),
                12,
                TimeUnit.HOURS
        );

        // 8️⃣ EVENTO KAFKA
        producer.sendVehicleEntry(
                new VehicleEntryEvent(
                        request.plate,
                        LocalDateTime.now().toString()
                )
        );

        return saved;
    }

    public ParkingEntry getActiveEntry(String plate) {
        return repository.findByPlateAndStatus(plate, EntryStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active entry found"));
    }

    public void closeEntry(String entryId) {

        ParkingEntry entry = repository.findById(UUID.fromString(entryId))
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        entry.close();
        repository.save(entry);

        // 🧹 ELIMINAR DE REDIS
        redisTemplate.delete(ACTIVE_ENTRY_KEY + entry.getPlate());
    }
}

