package com.university.parking.exit.kafka.producer;

import com.university.parking.exit.kafka.event.VehicleExitEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExitEventProducer {

    private final KafkaTemplate<String, VehicleExitEvent> kafkaTemplate;

    public ExitEventProducer(KafkaTemplate<String, VehicleExitEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendVehicleExit(VehicleExitEvent event) {
        kafkaTemplate.send("vehicle-exit", event);
    }
}

