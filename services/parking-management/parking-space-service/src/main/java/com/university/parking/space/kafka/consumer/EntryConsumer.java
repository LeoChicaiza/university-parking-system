package com.university.parking.space.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.parking.space.service.ParkingSpaceService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EntryConsumer {

    private final ParkingSpaceService parkingSpaceService;
    private final ObjectMapper objectMapper;

    public EntryConsumer(ParkingSpaceService parkingSpaceService, ObjectMapper objectMapper) {
        this.parkingSpaceService = parkingSpaceService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "vehicle-entry", groupId = "parking-space-group")
    public void consumeVehicleEntry(String message) {
        try {
            // Parsear mensaje como JSON simple
            Map<String, Object> event = objectMapper.readValue(message, Map.class);
            
            String plate = (String) event.get("plate");
            String lotId = (String) event.getOrDefault("lotId", "default-lot");
            
            // Asignar espacio
            parkingSpaceService.assignSpace(lotId);
            
            System.out.println("Assigned space for vehicle: " + plate + " in lot: " + lotId);
            
        } catch (Exception e) {
            System.err.println("Error processing vehicle entry event: " + e.getMessage());
        }
    }
}