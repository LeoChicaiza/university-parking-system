package com.university.parking.exit.controller;

import com.university.parking.exit.model.ExitRequest;
import com.university.parking.exit.service.ExitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExitController.class)
class ExitControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private ExitService exitService;
    
    @Test
    void processExit_ValidRequest_ShouldReturnOk() throws Exception {
        // Arrange
        ExitRequest request = new ExitRequest();
        request.plate = "ABC123";
        
        Map<String, Object> serviceResponse = Map.of(
            "exitId", "550e8400-e29b-41d4-a716-446655440000",
            "plate", "ABC123",
            "status", "EXIT_REGISTERED",
            "exitTime", System.currentTimeMillis()
        );
        
        when(exitService.processExit(anyString())).thenReturn(serviceResponse);
        
        // Act & Assert
        mockMvc.perform(post("/api/exit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plate").value("ABC123"))
                .andExpect(jsonPath("$.status").value("EXIT_REGISTERED"));
    }
    
    @Test
    void processExit_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        // Arrange
        ExitRequest request = new ExitRequest();
        request.plate = "ABC123";
        
        when(exitService.processExit(anyString()))
            .thenThrow(new RuntimeException("No active entry found"));
        
        // Act & Assert
        mockMvc.perform(post("/api/exit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("No active entry found"));
    }
    
    @Test
    void processExit_InvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Arrange - Empty plate
        ExitRequest request = new ExitRequest();
        request.plate = "";
        
        // Act & Assert
        mockMvc.perform(post("/api/exit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Plate is required"));
    }
    
    @Test
    void processExit_NullPlate_ShouldReturnBadRequest() throws Exception {
        // Arrange - Null plate
        ExitRequest request = new ExitRequest();
        request.plate = null;
        
        // Act & Assert
        mockMvc.perform(post("/api/exit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Plate is required"));
    }
}