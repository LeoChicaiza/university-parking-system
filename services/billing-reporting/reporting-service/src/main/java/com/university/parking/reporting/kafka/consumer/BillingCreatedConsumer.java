package com.university.parking.reporting.kafka.consumer;

import com.university.parking.billing.kafka.event.BillingCreatedEvent;
import com.university.parking.reporting.service.ReportingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BillingCreatedConsumer {

    private final ReportingService service;

    public BillingCreatedConsumer(ReportingService service) {
        this.service = service;
    }

    @KafkaListener(topics = "billing-created", groupId = "reporting-group")
    public void consume(BillingCreatedEvent event) {
        service.registerBilling(
                event.getPlate(),
                event.getAmount()
        );
    }
}
