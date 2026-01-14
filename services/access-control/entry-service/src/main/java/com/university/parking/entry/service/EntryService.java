package com.university.parking.entry.service;

import com.university.parking.entry.kafka.event.VehicleEntryEvent;
import com.university.parking.entry.kafka.producer.EntryEventProducer;
import com.university.parking.entry.model.EntryRecord;
import com.university.parking.entry.model.EntryRequest;
import com.university.parking.entry.repository.EntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class EntryService {

    private final EntryRepository repository;
    private final RestTemplate restTemplate;
    private final EntryEventProducer producer;

    // ✅ UN SOLO CONSTRUCTOR (correcto)
    public EntryService(
            EntryRepository repository,
            RestTemplate restTemplate,
            EntryEventProducer producer
    ) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.producer = producer;
    }

    // 🔹 ESTE ES EL MÉTODO CORRECTO
    public EntryRecord processEntry(EntryRequest request) {

        // 1️⃣ Validar que no esté ya dentro
        repository.findActiveByPlate(request.plate).ifPresent(r -> {
            throw new RuntimeException("Vehicle already inside");
        });

        // 2️⃣ Validar vehículo
        Boolean validVehicle = restTemplate.getForObject(
                "http://vehicle-service/vehicles/validate?plate={plate}&email={email}",
                Boolean.class,
                request.plate,
                request.userEmail
        );

        if (Boolean.FALSE.equals(validVehicle)) {
            throw new RuntimeException("Vehicle not authorized");
        }

        // 3️⃣ (TEMPORAL) Asignar espacio por REST
        var space = restTemplate.postForObject(
                "http://parking-space-service/parking-spaces/assign",
                Map.of("lotId", request.lotId),
                Map.class
        );

        String spaceId = (String) space.get("id");

        // 4️⃣ Ocupar capacidad general
        restTemplate.postForObject(
                "http://parking-lot-service/parking-lots/{id}/occupy",
                null,
                Void.class,
                request.lotId
        );

        // 5️⃣ REGISTRAR ENTRADA (estado interno)
        EntryRecord record = new EntryRecord(
                UUID.randomUUID().toString(),
                request.plate,
                spaceId,
                request.lotId,
                request.userEmail,
                System.currentTimeMillis()
        );

        EntryRecord saved = repository.save(record);

        // ✅ 6️⃣ AQUÍ VA KAFKA (LUGAR CORRECTO)
        producer.sendVehicleEntry(
                new VehicleEntryEvent(
                        request.plate,
                        LocalDateTime.now().toString()
                )
        );

        return saved;
    }

    public EntryRecord getActiveEntry(String plate) {
        return repository.findActiveByPlate(plate)
                .orElseThrow(() -> new RuntimeException("No active entry found"));
    }

    // ❌ NO SE EMITE EVENTO AQUÍ
    public void closeEntry(String entryId) {
        EntryRecord record = repository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found"));
        record.close();
    }
}
