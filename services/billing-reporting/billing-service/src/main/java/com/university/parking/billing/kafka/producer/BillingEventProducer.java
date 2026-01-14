package com.university.parking.billing.kafka.producer;

import com.university.parking.billing.kafka.event.BillingCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BillingEventProducer {

    private final KafkaTemplate<String, BillingCreatedEvent> kafkaTemplate;

    public BillingEventProducer(KafkaTemplate<String, BillingCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBillingCreated(BillingCreatedEvent event) {
        kafkaTemplate.send("billing-created", event);
    }
}

