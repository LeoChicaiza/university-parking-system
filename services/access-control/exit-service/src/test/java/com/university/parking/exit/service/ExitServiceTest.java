package com.university.parking.exit.service;

import com.university.parking.exit.kafka.event.VehicleExitEvent;
import com.university.parking.exit.kafka.producer.ExitEventProducer;
import com.university.parking.exit.model.ParkingExit;
import com.university.parking.exit.rabbitmq.ExitRabbitProducer;
import com.university.parking.exit.repository.ExitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExitServiceTest {

    @Mock
    private ExitRepository exitRepository;
    
    @Mock
    private ExitEventProducer kafkaProducer;
    
    @Mock
    private ExitRabbitProducer rabbitProducer;
    
    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private StringRedisTemplate redisTemplate;
    
    @InjectMocks
    private ExitService exitService;
    
    private final UUID mockEntryId = UUID.randomUUID();
    private final String testPlate = "ABC123";
    
    @BeforeEach
    void setUp() {
        // Configuración común para los tests
    }
    
    @Test
    void processExit_SuccessfulFlow() throws Exception {
        // Arrange
        Map<String, Object> mockEntryResponse = Map.of(
            "id", mockEntryId.toString(),
            "plate", testPlate,
            "status", "ACTIVE"
        );
        
        // Mock external service call
        when(restTemplate.getForObject(
                eq("http://entry-service/entry/active/{plate}"),
                eq(Map.class),
                eq(testPlate)
        )).thenReturn(mockEntryResponse);
        
        // Mock repository save with ID generation
        when(exitRepository.save(any(ParkingExit.class)))
                .thenAnswer(invocation -> {
                    ParkingExit exit = invocation.getArgument(0);
                    
                    // Use reflection to set ID since ParkingExit has no setId()
                    Field idField = ParkingExit.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    if (idField.get(exit) == null) {
                        idField.set(exit, UUID.randomUUID());
                    }
                    
                    return exit;
                });
        
        // CORREGIDO: redisTemplate.delete() devuelve Boolean, no Long
        when(redisTemplate.delete(anyString())).thenReturn(true);
        
        // Act
        Map<String, Object> result = exitService.processExit(testPlate);
        
        // Assert
        assertNotNull(result);
        assertEquals(testPlate, result.get("plate"));
        assertEquals("EXIT_REGISTERED", result.get("status"));
        assertNotNull(result.get("exitId"));
        assertNotNull(result.get("exitTime"));
        
        // Verify interactions
        verify(restTemplate, times(1)).getForObject(
                eq("http://entry-service/entry/active/{plate}"),
                eq(Map.class),
                eq(testPlate)
        );
        
        verify(exitRepository, times(1)).save(any(ParkingExit.class));
        verify(kafkaProducer, times(1)).sendVehicleExit(any(VehicleExitEvent.class));
        verify(rabbitProducer, times(1)).sendExitMessage(eq(testPlate));
        verify(redisTemplate, times(1)).delete(contains("active-entry:"));
    }
    
    @Test
    void processExit_WhenNoActiveEntryFound_ShouldThrowException() {
        // Arrange
        when(restTemplate.getForObject(
                eq("http://entry-service/entry/active/{plate}"),
                eq(Map.class),
                eq(testPlate)
        )).thenReturn(null);  // No active entry
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            exitService.processExit(testPlate);
        });
        
        assertTrue(exception.getMessage().contains("No active entry found for vehicle"));
        
        // Verify no further interactions
        verify(exitRepository, never()).save(any(ParkingExit.class));
        verify(kafkaProducer, never()).sendVehicleExit(any());
        verify(rabbitProducer, never()).sendExitMessage(anyString());
        verify(redisTemplate, never()).delete(anyString());
    }
    
    @Test
    void processExit_WhenEntryHasNoId_ShouldThrowException() {
        // Arrange
        Map<String, Object> mockEntryResponse = Map.of(
            "plate", testPlate,
            "status", "ACTIVE"
            // No "id" field
        );
        
        when(restTemplate.getForObject(
                eq("http://entry-service/entry/active/{plate}"),
                eq(Map.class),
                eq(testPlate)
        )).thenReturn(mockEntryResponse);
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            exitService.processExit(testPlate);
        });
        
        assertTrue(exception.getMessage().contains("No active entry found for vehicle"));
    }
    
    @Test
    void processExit_ShouldDeleteRedisKey() throws Exception {
        // Arrange
        Map<String, Object> mockEntryResponse = Map.of(
            "id", mockEntryId.toString(),
            "plate", testPlate,
            "status", "ACTIVE"
        );
        
        when(restTemplate.getForObject(
                eq("http://entry-service/entry/active/{plate}"),
                eq(Map.class),
                eq(testPlate)
        )).thenReturn(mockEntryResponse);
        
        when(exitRepository.save(any(ParkingExit.class)))
                .thenAnswer(invocation -> {
                    ParkingExit exit = invocation.getArgument(0);
                    Field idField = ParkingExit.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(exit, UUID.randomUUID());
                    return exit;
                });
        
        // CORREGIDO: Cambiado de thenReturn(1L) a thenReturn(true)
        when(redisTemplate.delete(anyString())).thenReturn(true);
        
        // Act
        exitService.processExit(testPlate);
        
        // Assert - Verify Redis key is deleted with correct format
        verify(redisTemplate, times(1)).delete("active-entry:" + testPlate);
    }
}