package com.university.parking.space.kafka.consumer;

import com.university.parking.space.kafka.event.VehicleEntryEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EntryConsumer {

    @KafkaListener(topics = "vehicle-entry", groupId = "parking-group")
    public void consume(VehicleEntryEvent event) {
        System.out.println("Assign parking space to " + event.getPlate());
    }
}
