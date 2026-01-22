package com.university.parking.lot.service;

import com.university.parking.lot.model.ParkingLot;
import com.university.parking.lot.repository.ParkingLotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingLotServiceTest {

    @Mock private ParkingLotRepository repository;
    @Mock private StringRedisTemplate redisTemplate;
    @Mock private ValueOperations<String, String> valueOperations;
    @InjectMocks private ParkingLotService parkingLotService;

    private final UUID testLotId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final String testLotIdString = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test void occupy_WhenSpaceAvailable_ShouldOccupySuccessfully() {
        String capacityKey = "parking-lot:" + testLotIdString + ":capacity";
        String occupiedKey = "parking-lot:" + testLotIdString + ":occupied";
        
        ParkingLot parkingLot = new ParkingLot("Main Lot", 100);
        parkingLot.setId(testLotId);
        
        when(valueOperations.get(capacityKey)).thenReturn("100");
        when(valueOperations.get(occupiedKey)).thenReturn("50");
        when(repository.findById(testLotIdString)).thenReturn(Optional.of(parkingLot));
        when(repository.save(any(ParkingLot.class))).thenReturn(parkingLot);

        parkingLotService.occupy(testLotIdString);

        verify(valueOperations, times(1)).get(capacityKey);
        verify(valueOperations, times(1)).get(occupiedKey);
        verify(valueOperations, times(1)).increment(occupiedKey);
        verify(repository, times(1)).findById(testLotIdString);
        verify(repository, times(1)).save(any(ParkingLot.class));
    }

    @Test void occupy_WhenLotIsFull_ShouldThrowException() {
        String capacityKey = "parking-lot:" + testLotIdString + ":capacity";
        String occupiedKey = "parking-lot:" + testLotIdString + ":occupied";
        
        when(valueOperations.get(capacityKey)).thenReturn("100");
        when(valueOperations.get(occupiedKey)).thenReturn("100");

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> parkingLotService.occupy(testLotIdString));
        
        assertEquals("Parking lot full", exception.getMessage());
        
        verify(valueOperations, times(1)).get(capacityKey);
        verify(valueOperations, times(1)).get(occupiedKey);
        verify(valueOperations, never()).increment(anyString());
        verify(repository, never()).findById(anyString());
    }

    @Test void occupy_WhenCapacityNotSetInRedis_ShouldThrowException() {
        String capacityKey = "parking-lot:" + testLotIdString + ":capacity";
        String occupiedKey = "parking-lot:" + testLotIdString + ":occupied";
        
        when(valueOperations.get(capacityKey)).thenReturn(null);
        when(valueOperations.get(occupiedKey)).thenReturn("50");

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> parkingLotService.occupy(testLotIdString));
        
        assertEquals("Parking lot capacity not set in Redis", exception.getMessage());
        
        verify(valueOperations, times(1)).get(capacityKey);
        verify(valueOperations, times(1)).get(occupiedKey);
        verify(valueOperations, never()).increment(anyString());
        verify(repository, never()).findById(anyString());
    }

    @Test void release_ShouldDecrementOccupancy() {
        String occupiedKey = "parking-lot:" + testLotIdString + ":occupied";
        
        ParkingLot parkingLot = new ParkingLot("Main Lot", 100);
        parkingLot.setId(testLotId);
        
        when(valueOperations.decrement(occupiedKey)).thenReturn(49L);
        when(repository.findById(testLotIdString)).thenReturn(Optional.of(parkingLot));
        when(repository.save(any(ParkingLot.class))).thenReturn(parkingLot);

        parkingLotService.release(testLotIdString);

        verify(valueOperations, times(1)).decrement(occupiedKey);
        verify(repository, times(1)).findById(testLotIdString);
        verify(repository, times(1)).save(any(ParkingLot.class));
    }

    @Test void release_WhenOccupancyNegative_ShouldSetToZero() {
        String occupiedKey = "parking-lot:" + testLotIdString + ":occupied";
        
        ParkingLot parkingLot = new ParkingLot("Main Lot", 100);
        parkingLot.setId(testLotId);
        
        when(valueOperations.decrement(occupiedKey)).thenReturn(-1L);
        when(repository.findById(testLotIdString)).thenReturn(Optional.of(parkingLot));
        when(repository.save(any(ParkingLot.class))).thenReturn(parkingLot);

        parkingLotService.release(testLotIdString);

        verify(valueOperations, times(1)).decrement(occupiedKey);
        verify(valueOperations, times(1)).set(occupiedKey, "0");
        verify(repository, times(1)).findById(testLotIdString);
        verify(repository, times(1)).save(any(ParkingLot.class));
    }

    @Test void createParkingLot_ShouldCreateAndInitializeRedis() {
        String name = "Test Lot";
        int capacity = 50;
        
        ParkingLot parkingLot = new ParkingLot(name, capacity);
        UUID generatedId = UUID.randomUUID();
        parkingLot.setId(generatedId);
        
        String expectedLotIdString = generatedId.toString();
        String capacityKey = "parking-lot:" + expectedLotIdString + ":capacity";
        String occupiedKey = "parking-lot:" + expectedLotIdString + ":occupied";
        
        when(repository.save(any(ParkingLot.class))).thenReturn(parkingLot);

        ParkingLot result = parkingLotService.createParkingLot(name, capacity);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(capacity, result.getCapacity());
        
        verify(repository, times(1)).save(any(ParkingLot.class));
        verify(valueOperations, times(1)).set(capacityKey, String.valueOf(capacity));
        verify(valueOperations, times(1)).set(occupiedKey, "0");
    }

    @Test void getParkingLot_WhenExists_ShouldReturnLot() {
        ParkingLot expectedLot = new ParkingLot("Main Lot", 100);
        expectedLot.setId(testLotId);
        
        when(repository.findById(testLotIdString)).thenReturn(Optional.of(expectedLot));

        ParkingLot result = parkingLotService.getParkingLot(testLotIdString);

        assertNotNull(result);
        assertEquals(expectedLot.getId(), result.getId());
        verify(repository, times(1)).findById(testLotIdString);
    }

    @Test void getParkingLot_WhenNotExists_ShouldThrowException() {
        when(repository.findById(testLotIdString)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> parkingLotService.getParkingLot(testLotIdString));
        
        assertEquals("Lot not found: " + testLotIdString, exception.getMessage());
        verify(repository, times(1)).findById(testLotIdString);
    }
}