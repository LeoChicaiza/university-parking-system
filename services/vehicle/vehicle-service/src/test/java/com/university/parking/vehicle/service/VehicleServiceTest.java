package com.university.parking.vehicle.service;

import com.university.parking.vehicle.model.Vehicle;
import com.university.parking.vehicle.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void registerVehicle_WithValidVehicle_ShouldSaveAndReturnVehicle() {
        // Arrange
        Vehicle vehicleToRegister = new Vehicle("ABC123", "Toyota", "Corolla", "owner@university.edu");
        
        Vehicle savedVehicle = new Vehicle("ABC123", "Toyota", "Corolla", "owner@university.edu");
        savedVehicle.setId(UUID.randomUUID());
        
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(savedVehicle);

        // Act
        Vehicle result = vehicleService.registerVehicle(vehicleToRegister);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("ABC123", result.getPlate());
        assertEquals("Toyota", result.getBrand());
        assertEquals("Corolla", result.getModel());
        assertEquals("owner@university.edu", result.getOwnerEmail());
        
        verify(vehicleRepository, times(1)).save(vehicleToRegister);
    }

    @Test
    void validateOwnership_WhenVehicleExistsAndEmailMatches_ShouldReturnTrue() {
        // Arrange
        String plate = "ABC123";
        String ownerEmail = "correct.owner@university.edu";
        
        Vehicle vehicle = new Vehicle(plate, "Toyota", "Corolla", ownerEmail);
        
        when(vehicleRepository.findByPlate(plate)).thenReturn(Optional.of(vehicle));

        // Act
        boolean result = vehicleService.validateOwnership(plate, ownerEmail);

        // Assert
        assertTrue(result);
        verify(vehicleRepository, times(1)).findByPlate(plate);
    }

    @Test
    void validateOwnership_WhenVehicleExistsButEmailDoesNotMatch_ShouldReturnFalse() {
        // Arrange
        String plate = "ABC123";
        String correctOwner = "correct@university.edu";
        String wrongOwner = "wrong@university.edu";
        
        Vehicle vehicle = new Vehicle(plate, "Toyota", "Corolla", correctOwner);
        
        when(vehicleRepository.findByPlate(plate)).thenReturn(Optional.of(vehicle));

        // Act
        boolean result = vehicleService.validateOwnership(plate, wrongOwner);

        // Assert
        assertFalse(result);
        verify(vehicleRepository, times(1)).findByPlate(plate);
    }

    @Test
    void validateOwnership_WhenVehicleDoesNotExist_ShouldReturnFalse() {
        // Arrange
        String plate = "NONEXISTENT123";
        String anyEmail = "any@university.edu";
        
        when(vehicleRepository.findByPlate(plate)).thenReturn(Optional.empty());

        // Act
        boolean result = vehicleService.validateOwnership(plate, anyEmail);

        // Assert
        assertFalse(result);
        verify(vehicleRepository, times(1)).findByPlate(plate);
    }

    @Test
    void getVehiclesByOwner_WhenOwnerHasVehicles_ShouldReturnList() {
        // Arrange
        String ownerEmail = "owner@university.edu";
        
        Vehicle vehicle1 = new Vehicle("ABC123", "Toyota", "Corolla", ownerEmail);
        Vehicle vehicle2 = new Vehicle("XYZ789", "Honda", "Civic", ownerEmail);
        List<Vehicle> expectedVehicles = Arrays.asList(vehicle1, vehicle2);
        
        // CORRECCIÓN: Usar findByOwner (no findByOwnerEmail)
        when(vehicleRepository.findByOwner(ownerEmail)).thenReturn(expectedVehicles);

        // Act
        List<Vehicle> result = vehicleService.getVehiclesByOwner(ownerEmail);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ABC123", result.get(0).getPlate());
        assertEquals("XYZ789", result.get(1).getPlate());
        verify(vehicleRepository, times(1)).findByOwner(ownerEmail);
    }

    @Test
    void getVehiclesByOwner_WhenOwnerHasNoVehicles_ShouldReturnEmptyList() {
        // Arrange
        String ownerEmail = "no.vehicles@university.edu";
        List<Vehicle> emptyList = Arrays.asList();
        
        // CORRECCIÓN: Usar findByOwner (no findByOwnerEmail)
        when(vehicleRepository.findByOwner(ownerEmail)).thenReturn(emptyList);

        // Act
        List<Vehicle> result = vehicleService.getVehiclesByOwner(ownerEmail);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(vehicleRepository, times(1)).findByOwner(ownerEmail);
    }
}