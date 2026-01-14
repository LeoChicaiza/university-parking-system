package com.university.parking.notification.kafka.consumer;

import com.university.parking.notification.service.NotificationService;
import com.university.parking.billing.kafka.event.BillingCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BillingCreatedConsumer {

    private final NotificationService service;

    public BillingCreatedConsumer(NotificationService service) {
        this.service = service;
    }

    @KafkaListener(
        topics = "billing-created",
        groupId = "notification-group"
    )
    public void consume(BillingCreatedEvent event) {

        service.createBillingNotification(
                event.getPlate(),
                event.getAmount()
        );
    }
}
