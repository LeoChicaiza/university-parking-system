package com.university.parking.reporting.kafka.consumer;

import com.university.parking.reporting.kafka.event.BillingCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BillingReportConsumer {

    @KafkaListener(topics = "billing-created", groupId = "reporting-group")
    public void consume(BillingCreatedEvent event) {

        // Simulación de persistencia para reportes
        System.out.println(
            "REPORTING → Plate: " + event.getPlate()
            + " | Amount: $" + event.getAmount()
        );

        // Aquí luego puedes guardar en BD
    }
}


