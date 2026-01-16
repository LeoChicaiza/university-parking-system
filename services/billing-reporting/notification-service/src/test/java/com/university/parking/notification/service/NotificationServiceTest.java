package com.university.parking.notification.service;

import com.university.parking.notification.model.Notification;
import com.university.parking.notification.model.NotificationRequest;
import com.university.parking.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    
    @InjectMocks
    private NotificationService notificationService;
    
    @Test
    void createBillingNotification_ShouldSaveNotificationWithCorrectData() throws Exception {
        // Arrange
        String testPlate = "ABC123";
        double testAmount = 25.75;
        
        when(notificationRepository.save(any(Notification.class)))
            .thenAnswer(invocation -> {
                Notification notif = invocation.getArgument(0);
                // Usar reflection para setear ID (porque Notification no tiene setId)
                Field idField = Notification.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(notif, UUID.randomUUID());
                
                // También setear createdAt si es necesario
                Field createdAtField = Notification.class.getDeclaredField("createdAt");
                createdAtField.setAccessible(true);
                createdAtField.set(notif, LocalDateTime.now());
                
                return notif;
            });
        
        // Act
        notificationService.createBillingNotification(testPlate, testAmount);
        
        // Assert
        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository, times(1)).save(captor.capture());
        
        Notification savedNotification = captor.getValue();
        assertEquals(testPlate, savedNotification.getPlate());
        assertEquals(testAmount, savedNotification.getAmount());
        assertTrue(savedNotification.getMessage().contains(testPlate));
        assertTrue(savedNotification.getMessage().contains("$" + testAmount));
        assertNotNull(savedNotification.getCreatedAt());
    }
    
    @Test
    void send_ValidNotificationRequest_ShouldReturnTrueAndSave() throws Exception {
        // Arrange
        NotificationRequest request = new NotificationRequest();
        request.setUserId("user-001");
        request.setType("BILLING");
        request.setMessage("Vehicle XYZ789 billed $15.50");
        
        when(notificationRepository.save(any(Notification.class)))
            .thenAnswer(invocation -> {
                Notification notif = invocation.getArgument(0);
                Field idField = Notification.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(notif, UUID.randomUUID());
                return notif;
            });
        
        // Act
        boolean result = notificationService.send(request);
        
        // Assert
        assertTrue(result);
        
        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository, times(1)).save(captor.capture());
        
        Notification savedNotification = captor.getValue();
        assertEquals("XYZ789", savedNotification.getPlate()); // Extraído del mensaje
        assertEquals(15.50, savedNotification.getAmount()); // Extraído del mensaje
        assertTrue(savedNotification.getMessage().contains("[BILLING]"));
        assertTrue(savedNotification.getMessage().contains("user-001"));
        assertTrue(savedNotification.getMessage().contains("Vehicle XYZ789 billed $15.50"));
    }
    
    @Test
    void send_NullRequest_ShouldReturnFalse() {
        // Arrange
        NotificationRequest request = null;
        
        // Act
        boolean result = notificationService.send(request);
        
        // Assert
        assertFalse(result);
        verify(notificationRepository, never()).save(any(Notification.class));
    }
    
    @Test
    void send_RequestWithoutPlateInMessage_ShouldUseUnknownPlate() throws Exception {
        // Arrange
        NotificationRequest request = new NotificationRequest();
        request.setUserId("user-002");
        request.setType("INFO");
        request.setMessage("Welcome to the parking system");
        
        when(notificationRepository.save(any(Notification.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        boolean result = notificationService.send(request);
        
        // Assert
        assertTrue(result);
        
        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository, times(1)).save(captor.capture());
        
        Notification savedNotification = captor.getValue();
        assertEquals("UNKNOWN", savedNotification.getPlate());
        assertEquals(0.0, savedNotification.getAmount());
        assertTrue(savedNotification.getMessage().contains("[INFO]"));
        assertTrue(savedNotification.getMessage().contains("user-002"));
    }
    
    @Test
    void send_RequestWithInvalidAmountFormat_ShouldUseZeroAmount() throws Exception {
        // Arrange
        NotificationRequest request = new NotificationRequest();
        request.setUserId("user-003");
        request.setType("BILLING");
        request.setMessage("Vehicle ABC123 billed INVALID_AMOUNT");
        
        when(notificationRepository.save(any(Notification.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        boolean result = notificationService.send(request);
        
        // Assert
        assertTrue(result);
        
        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository, times(1)).save(captor.capture());
        
        Notification savedNotification = captor.getValue();
        assertEquals("ABC123", savedNotification.getPlate());
        assertEquals(0.0, savedNotification.getAmount()); // No pudo extraer amount
    }
}