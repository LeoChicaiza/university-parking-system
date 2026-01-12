package com.university.parking.billing.kafka.consumer;

import com.university.parking.billing.kafka.event.VehicleExitEvent;
import com.university.parking.billing.kafka.event.BillingCreatedEvent;
import com.university.parking.billing.kafka.producer.BillingEventProducer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class VehicleExitConsumer {

    private final BillingEventProducer producer;

    public VehicleExitConsumer(BillingEventProducer producer) {
        this.producer = producer;
    }

    @KafkaListener(topics = "vehicle-exit", groupId = "billing-group")
    public void consume(VehicleExitEvent event) {

        // 1️⃣ Calcular monto (lógica simple académica)
        double amount = calculateAmount(event);

        // 2️⃣ Emitir evento billing-created
        BillingCreatedEvent billingEvent =
                new BillingCreatedEvent(event.getPlate(), amount);

        producer.sendBillingCreated(billingEvent);
    }

    private double calculateAmount(VehicleExitEvent event) {
        // Lógica simplificada para proyecto universitario
        return 2.50;
    }
}
