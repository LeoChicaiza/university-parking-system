package com.university.parking.entry.service;

import com.university.parking.entry.kafka.event.VehicleEntryEvent;
import com.university.parking.entry.kafka.producer.EntryEventProducer;
import com.university.parking.entry.model.EntryRequest;
import com.university.parking.entry.model.EntryStatus;
import com.university.parking.entry.model.ParkingEntry;
import com.university.parking.entry.repository.EntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EntryServiceTest {

    private EntryRepository repository;
    private RestTemplate restTemplate;
    private EntryEventProducer producer;
    private StringRedisTemplate redisTemplate;
    private ValueOperations<String, String> valueOps;

    private EntryService service;

    @BeforeEach
    void setup() {
        repository = mock(EntryRepository.class);
        restTemplate = mock(RestTemplate.class);
        producer = mock(EntryEventProducer.class);
        redisTemplate = mock(StringRedisTemplate.class);
        valueOps = mock(ValueOperations.class);

        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        service = new EntryService(
                repository,
                restTemplate,
                producer,
                redisTemplate
        );
    }

    @Test
    void processEntry_successfulFlow() throws Exception {

        // 🔹 REQUEST
        EntryRequest request = new EntryRequest();
        request.plate = "ABC123";
        request.lotId = "LOT-1";
        request.userEmail = "test@uce.edu.ec";

        // 🔹 REDIS: no existe entrada activa
        when(redisTemplate.hasKey("active-entry:ABC123")).thenReturn(false);

        // 🔹 BD: no existe entrada activa
        when(repository.findByPlateAndStatus("ABC123", EntryStatus.ACTIVE))
                .thenReturn(Optional.empty());

        // 🔹 VEHICLE SERVICE
        when(restTemplate.getForObject(
                anyString(),
                eq(Boolean.class),
                any(),
                any()
        )).thenReturn(true);

        // 🔹 PARKING SPACE SERVICE
        when(restTemplate.postForObject(
                contains("parking-spaces/assign"),
                ArgumentMatchers.<Map<String, String>>any(),
                eq(Map.class)
        )).thenReturn(Map.of("id", "SPACE-9"));

        // 🔹 PARKING LOT SERVICE
        when(restTemplate.postForObject(
                contains("parking-lots"),
                isNull(),
                eq(Void.class),
                ArgumentMatchers.<Object[]>any()
        )).thenReturn(null);

        // 🔹 SAVE ENTRY - SOLUCIÓN CON REFLECTION
        when(repository.save(any(ParkingEntry.class)))
                .thenAnswer(invocation -> {
                    ParkingEntry entry = invocation.getArgument(0);
                    
                    // Usar Reflection para asignar un ID
                    Field idField = ParkingEntry.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    
                    if (idField.get(entry) == null) {
                        idField.set(entry, UUID.randomUUID());
                    }
                    
                    return entry;
                });

        // 🧪 EJECUCIÓN
        ParkingEntry result = service.processEntry(request);

        // ✅ ASSERTS
        assertNotNull(result);
        assertNotNull(result.getId(), "ParkingEntry ID should not be null");
        assertEquals("ABC123", result.getPlate());
        assertEquals("LOT-1", result.getLotId());
        assertEquals(EntryStatus.ACTIVE, result.getStatus());

        // 🔍 VERIFY
        verify(repository).save(any(ParkingEntry.class));
        verify(producer).sendVehicleEntry(any(VehicleEntryEvent.class));
        verify(valueOps).set(
                startsWith("active-entry:"),
                anyString(),
                eq(12L),
                any()
        );
    }
}