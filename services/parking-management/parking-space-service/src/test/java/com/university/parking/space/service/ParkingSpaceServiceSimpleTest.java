package com.university.parking.space.service;

import com.university.parking.space.model.ParkingSpace;
import com.university.parking.space.repository.ParkingSpaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingSpaceServiceSimpleTest {

    @Mock
    private ParkingSpaceRepository repository;

    @InjectMocks
    private ParkingSpaceService parkingSpaceService;

    @Test
    void assignSpace_WhenSpaceAvailable_ShouldAssignSuccessfully() {
        // Arrange
        String lotId = "lot-001";
        String spaceId = "space-001";
        
        ParkingSpace parkingSpace = new ParkingSpace(spaceId, lotId);
        
        when(repository.findFirstByLotIdAndOccupiedFalse(lotId))
            .thenReturn(Optional.of(parkingSpace));
        when(repository.save(any(ParkingSpace.class))).thenReturn(parkingSpace);

        // Act
        ParkingSpace result = parkingSpaceService.assignSpace(lotId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isOccupied());
        assertEquals(spaceId, result.getId());
        
        verify(repository, times(1)).findFirstByLotIdAndOccupiedFalse(lotId);
        verify(repository, times(1)).save(parkingSpace);
    }

    @Test
    void assignSpace_WhenNoSpaceAvailable_ShouldThrowException() {
        // Arrange
        String lotId = "lot-001";
        
        when(repository.findFirstByLotIdAndOccupiedFalse(lotId))
            .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> parkingSpaceService.assignSpace(lotId));
        
        assertEquals("No available spaces", exception.getMessage());
        
        verify(repository, times(1)).findFirstByLotIdAndOccupiedFalse(lotId);
        verify(repository, never()).save(any());
    }
}