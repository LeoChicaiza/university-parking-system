package com.university.parking.reporting.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.parking.reporting.kafka.event.VehicleEntryEvent; // Import LOCAL
import com.university.parking.reporting.service.ReportingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class VehicleEntryConsumer {

    private final ReportingService reportingService;
    private final ObjectMapper objectMapper;

    public VehicleEntryConsumer(ReportingService reportingService, ObjectMapper objectMapper) {
        this.reportingService = reportingService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "vehicle-entry", groupId = "reporting-group")
    public void consumeVehicleEntry(String message, Acknowledgment acknowledgment) {
        try {
            // Deserializar el mensaje JSON a objeto
            VehicleEntryEvent event = objectMapper.readValue(message, VehicleEntryEvent.class);
            
            // Registrar la entrada
            reportingService.registerEntry(event.getPlate(), event.getEntryTime());
            
            System.out.println("📝 CONSUMED ENTRY EVENT: " + event.getPlate() + 
                              " at " + event.getEntryTime() + 
                              " in zone " + event.getParkingZone());
            
        } catch (Exception e) {
            System.err.println("❌ Error processing vehicle entry event: " + e.getMessage());
            System.err.println("Message was: " + message);
        } finally {
            acknowledgment.acknowledge();
        }
    }
}