package com.university.parking.entry.kafka.producer;

import com.university.parking.entry.kafka.event.VehicleEntryEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EntryEventProducer {

    private final KafkaTemplate<String, VehicleEntryEvent> kafkaTemplate;

    public EntryEventProducer(KafkaTemplate<String, VehicleEntryEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendVehicleEntry(VehicleEntryEvent event) {
        kafkaTemplate.send("vehicle-entry", event);
    }
}
