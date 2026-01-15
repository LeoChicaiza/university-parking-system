package com.university.parking.billing.kafka.consumer;

import com.university.parking.billing.kafka.event.VehicleExitEvent;
import com.university.parking.billing.kafka.event.BillingCreatedEvent;
import com.university.parking.billing.kafka.producer.BillingEventProducer;
import com.university.parking.billing.model.BillingRecord;
import com.university.parking.billing.service.BillingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class VehicleExitConsumer {

    private final BillingService billingService;
    private final BillingEventProducer producer;

    public VehicleExitConsumer(
            BillingService billingService,
            BillingEventProducer producer
    ) {
        this.billingService = billingService;
        this.producer = producer;
    }

    @KafkaListener(topics = "vehicle-exit", groupId = "billing-group")
    public void consume(VehicleExitEvent event) {

        // 1️⃣ Procesar billing completo
        BillingRecord record =
                billingService.processBilling(
                        event.getPlate(),
                        event.getExitTimeMillis()
                );

        // 2️⃣ Emitir evento billing-created
        BillingCreatedEvent billingEvent =
                new BillingCreatedEvent(
                        record.getPlate(),
                        record.getAmount()
                );

        producer.sendBillingCreated(billingEvent);
    }
}
