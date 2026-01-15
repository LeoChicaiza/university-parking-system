package com.university.parking.reporting.kafka.consumer;

import com.university.parking.entry.kafka.event.VehicleEntryEvent;
import com.university.parking.reporting.service.ReportingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class VehicleEntryConsumer {

    private final ReportingService service;

    public VehicleEntryConsumer(ReportingService service) {
        this.service = service;
    }

    @KafkaListener(topics = "vehicle-entry", groupId = "reporting-group")
    public void consume(VehicleEntryEvent event) {
        service.registerEntry(
                event.getPlate(),
                LocalDateTime.parse(event.getEntryTime())
        );
    }
}
