package com.university.parking.lot.controller;

import com.university.parking.lot.service.ParkingLotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ParkingLotControllerTest {

    @Mock
    private ParkingLotService parkingLotService;

    @InjectMocks
    private ParkingLotController parkingLotController;

    private MockMvc mockMvc;

    @Test
    void occupy_ValidLotId_ShouldReturnOk() throws Exception {
        // Arrange
        String lotId = "lot-001";
        mockMvc = MockMvcBuilders.standaloneSetup(parkingLotController).build();
        
        doNothing().when(parkingLotService).occupy(lotId);

        // Act & Assert
        mockMvc.perform(post("/parking-lots/{id}/occupy", lotId))
                .andExpect(status().isOk());
    }

    @Test
    void occupy_LotFull_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String lotId = "lot-001";
        mockMvc = MockMvcBuilders.standaloneSetup(parkingLotController).build();
        
        doThrow(new RuntimeException("Parking lot full"))
            .when(parkingLotService).occupy(lotId);

        // Act & Assert - Cambia a isBadRequest() en lugar de is5xxServerError()
        mockMvc.perform(post("/parking-lots/{id}/occupy", lotId))
                .andExpect(status().isBadRequest()); // 400
    }

    @Test
    void release_ValidLotId_ShouldReturnOk() throws Exception {
        // Arrange
        String lotId = "lot-001";
        mockMvc = MockMvcBuilders.standaloneSetup(parkingLotController).build();
        
        doNothing().when(parkingLotService).release(lotId);

        // Act & Assert
        mockMvc.perform(post("/parking-lots/{id}/release", lotId))
                .andExpect(status().isOk());
    }
}