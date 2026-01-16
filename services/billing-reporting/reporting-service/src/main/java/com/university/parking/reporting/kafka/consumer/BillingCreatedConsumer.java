package com.university.parking.reporting.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.parking.reporting.kafka.event.BillingCreatedEvent; // Import LOCAL
import com.university.parking.reporting.service.ReportingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class BillingCreatedConsumer {

    private final ReportingService reportingService;
    private final ObjectMapper objectMapper;

    public BillingCreatedConsumer(ReportingService reportingService, ObjectMapper objectMapper) {
        this.reportingService = reportingService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "billing-created", groupId = "reporting-group")
    public void consumeBillingCreated(String message, Acknowledgment acknowledgment) {
        try {
            // Deserializar el mensaje JSON a objeto
            BillingCreatedEvent event = objectMapper.readValue(message, BillingCreatedEvent.class);
            
            // Registrar la facturación
            reportingService.registerBilling(event.getPlate(), event.getAmount());
            
            System.out.println("💰 CONSUMED BILLING EVENT: " + event.getPlate() + 
                              " - Amount: $" + event.getAmount() +
                              " - Billing ID: " + event.getBillingId());
            
        } catch (Exception e) {
            System.err.println("❌ Error processing billing created event: " + e.getMessage());
            System.err.println("Message was: " + message);
        } finally {
            acknowledgment.acknowledge();
        }
    }
}