package com.university.parking.notification.kafka.consumer;

import com.university.parking.notification.kafka.event.BillingCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BillingConsumer {

    @KafkaListener(topics = "billing-created", groupId = "notification-group")
    public void consume(BillingCreatedEvent event) {

        // Simulación de notificación
        System.out.println(
            "NOTIFICATION → Vehicle " + event.getPlate()
            + " charged $" + event.getAmount()
        );
    }
}

