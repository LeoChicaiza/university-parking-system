package com.university.parking.reporting.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.parking.reporting.kafka.event.BillingCreatedEvent;
import com.university.parking.reporting.service.ReportingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingCreatedConsumerTest {

    @Mock
    private ReportingService reportingService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Acknowledgment acknowledgment;

    private BillingCreatedConsumer billingCreatedConsumer;

    @BeforeEach
    void setUp() {
        billingCreatedConsumer = new BillingCreatedConsumer(reportingService, objectMapper);
    }

    @Test
    void consumeBillingCreated_ValidEvent_ShouldProcessCorrectly() throws Exception {
        // Arrange - Crear evento de prueba
        String billingId = "bill-001";
        String plate = "ABC123";
        double amount = 25.75;
        
        BillingCreatedEvent event = new BillingCreatedEvent();
        event.setBillingId(billingId);
        event.setPlate(plate);
        event.setAmount(amount);
        event.setEntryTime(LocalDateTime.now().minusHours(2));
        event.setExitTime(LocalDateTime.now());
        event.setParkingZone("ZONE-A");
        
        String jsonMessage = "{\"billingId\":\"bill-001\",\"plate\":\"ABC123\",\"amount\":25.75}";
        
        when(objectMapper.readValue(jsonMessage, BillingCreatedEvent.class)).thenReturn(event);

        // Act - Consumir mensaje
        billingCreatedConsumer.consumeBillingCreated(jsonMessage, acknowledgment);

        // Assert - Verificar procesamiento
        ArgumentCaptor<String> plateCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Double> amountCaptor = ArgumentCaptor.forClass(Double.class);
        
        verify(reportingService, times(1))
            .registerBilling(plateCaptor.capture(), amountCaptor.capture());
        
        assertEquals(plate, plateCaptor.getValue(), "Placa incorrecta");
        assertEquals(amount, amountCaptor.getValue(), 0.01, "Monto incorrecto");
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    void consumeBillingCreated_InvalidJson_ShouldAcknowledgeAnyway() throws Exception {
        // Arrange - JSON inválido
        String invalidJson = "{invalid json";
        
        when(objectMapper.readValue(invalidJson, BillingCreatedEvent.class))
            .thenThrow(JsonProcessingException.class);

        // Act - Intentar consumir mensaje inválido
        billingCreatedConsumer.consumeBillingCreated(invalidJson, acknowledgment);

        // Assert - Debería acknowledge aunque falle
        verify(reportingService, never()).registerBilling(anyString(), anyDouble());
        verify(acknowledgment, times(1)).acknowledge();
    }
}