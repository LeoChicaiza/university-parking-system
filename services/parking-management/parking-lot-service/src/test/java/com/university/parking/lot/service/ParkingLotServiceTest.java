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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingLotServiceTest {

    @Mock
    private ParkingLotRepository repository;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private ParkingLotService parkingLotService;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void occupy_WhenSpaceAvailable_ShouldOccupySuccessfully() {
        // Arrange
        String lotId = "lot-001";
        String capacityKey = "parking-lot:" + lotId + ":capacity";
        String occupiedKey = "parking-lot:" + lotId + ":occupied";
        
        ParkingLot parkingLot = new ParkingLot(lotId, "Main Lot", 100);
        
        // Mock Redis values
        when(valueOperations.get(capacityKey)).thenReturn("100");
        when(valueOperations.get(occupiedKey)).thenReturn("50");
        
        // Mock repository
        when(repository.findById(lotId)).thenReturn(Optional.of(parkingLot));
        when(repository.save(any(ParkingLot.class))).thenReturn(parkingLot);

        // Act
        parkingLotService.occupy(lotId);

        // Assert
        verify(valueOperations, times(1)).get(capacityKey);
        verify(valueOperations, times(1)).get(occupiedKey);
        verify(valueOperations, times(1)).increment(occupiedKey);
        verify(repository, times(1)).findById(lotId);
        verify(repository, times(1)).save(any(ParkingLot.class));
    }

    @Test
    void occupy_WhenLotIsFull_ShouldThrowException() {
        // Arrange
        String lotId = "lot-001";
        String capacityKey = "parking-lot:" + lotId + ":capacity";
        String occupiedKey = "parking-lot:" + lotId + ":occupied";
        
        // Mock Redis values - lot is full
        when(valueOperations.get(capacityKey)).thenReturn("100");
        when(valueOperations.get(occupiedKey)).thenReturn("100");

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> parkingLotService.occupy(lotId));
        
        assertEquals("Parking lot full", exception.getMessage());
        
        verify(valueOperations, times(1)).get(capacityKey);
        verify(valueOperations, times(1)).get(occupiedKey);
        verify(valueOperations, never()).increment(anyString());
        verify(repository, never()).findById(anyString());
    }
}