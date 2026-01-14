package com.university.parking.reporting.kafka.consumer;

import com.university.parking.exit.kafka.event.VehicleExitEvent;
import com.university.parking.reporting.service.ReportingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class VehicleExitConsumer {

    private final ReportingService service;

    public VehicleExitConsumer(ReportingService service) {
        this.service = service;
    }

    @KafkaListener(topics = "vehicle-exit", groupId = "reporting-group")
    public void consume(VehicleExitEvent event) {
        service.registerExit(
                event.getPlate(),
                LocalDateTime.parse(event.getExitTime())
        );
    }
}
