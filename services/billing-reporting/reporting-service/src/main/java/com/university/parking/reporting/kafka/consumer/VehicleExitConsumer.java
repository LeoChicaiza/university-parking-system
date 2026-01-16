package com.university.parking.reporting.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.parking.reporting.kafka.event.VehicleExitEvent; // Import LOCAL
import com.university.parking.reporting.service.ReportingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class VehicleExitConsumer {

    private final ReportingService reportingService;
    private final ObjectMapper objectMapper;

    public VehicleExitConsumer(ReportingService reportingService, ObjectMapper objectMapper) {
        this.reportingService = reportingService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "vehicle-exit", groupId = "reporting-group")
    public void consumeVehicleExit(String message, Acknowledgment acknowledgment) {
        try {
            // Deserializar el mensaje JSON a objeto
            VehicleExitEvent event = objectMapper.readValue(message, VehicleExitEvent.class);
            
            // Registrar la salida
            reportingService.registerExit(event.getPlate(), event.getExitTime());
            
            System.out.println("📝 CONSUMED EXIT EVENT: " + event.getPlate() + 
                              " at " + event.getExitTime() + 
                              " from zone " + event.getParkingZone());
            
        } catch (Exception e) {
            System.err.println("❌ Error processing vehicle exit event: " + e.getMessage());
            System.err.println("Message was: " + message);
        } finally {
            acknowledgment.acknowledge();
        }
    }
}