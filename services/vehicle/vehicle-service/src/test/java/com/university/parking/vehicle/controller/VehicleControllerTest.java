package com.university.parking.vehicle.controller;

import com.university.parking.vehicle.model.Vehicle;
import com.university.parking.vehicle.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    @Test
    void register_WithValidVehicle_ShouldReturnVehicle() {
        // Arrange
        Vehicle vehicleToRegister = new Vehicle("ABC123", "Toyota", "Corolla", "owner@university.edu");
        
        Vehicle registeredVehicle = new Vehicle("ABC123", "Toyota", "Corolla", "owner@university.edu");
        registeredVehicle.setId(UUID.randomUUID());
        
        when(vehicleService.registerVehicle(any(Vehicle.class))).thenReturn(registeredVehicle);

        // Act
        Vehicle result = vehicleController.register(vehicleToRegister);

        // Assert
        assertNotNull(result);
        assertEquals("ABC123", result.getPlate());
        assertEquals("Toyota", result.getBrand());
        assertEquals("Corolla", result.getModel());
        assertEquals("owner@university.edu", result.getOwnerEmail());
        
        verify(vehicleService, times(1)).registerVehicle(vehicleToRegister);
    }

    @Test
    void getByOwner_ShouldReturnOwnerVehicles() {
        // Arrange
        String ownerEmail = "owner@university.edu";
        
        Vehicle vehicle1 = new Vehicle("ABC123", "Toyota", "Corolla", ownerEmail);
        Vehicle vehicle2 = new Vehicle("XYZ789", "Honda", "Civic", ownerEmail);
        List<Vehicle> expectedVehicles = Arrays.asList(vehicle1, vehicle2);
        
        when(vehicleService.getVehiclesByOwner(ownerEmail)).thenReturn(expectedVehicles);

        // Act
        List<Vehicle> result = vehicleController.getByOwner(ownerEmail);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ABC123", result.get(0).getPlate());
        assertEquals("XYZ789", result.get(1).getPlate());
        assertEquals(ownerEmail, result.get(0).getOwnerEmail());
        assertEquals(ownerEmail, result.get(1).getOwnerEmail());
        
        verify(vehicleService, times(1)).getVehiclesByOwner(ownerEmail);
    }

    @Test
    void validateOwnership_WhenOwnershipIsValid_ShouldReturnTrue() {
        // Arrange
        String plate = "ABC123";
        String ownerEmail = "valid.owner@university.edu";
        
        when(vehicleService.validateOwnership(plate, ownerEmail)).thenReturn(true);

        // Act
        boolean result = vehicleController.validateOwnership(plate, ownerEmail);

        // Assert
        assertTrue(result);
        verify(vehicleService, times(1)).validateOwnership(plate, ownerEmail);
    }

    @Test
    void validateOwnership_WhenOwnershipIsInvalid_ShouldReturnFalse() {
        // Arrange
        String plate = "ABC123";
        String wrongOwnerEmail = "wrong.owner@university.edu";
        
        when(vehicleService.validateOwnership(plate, wrongOwnerEmail)).thenReturn(false);

        // Act
        boolean result = vehicleController.validateOwnership(plate, wrongOwnerEmail);

        // Assert
        assertFalse(result);
        verify(vehicleService, times(1)).validateOwnership(plate, wrongOwnerEmail);
    }
}