package com.university.parking.reporting.service;

import com.university.parking.reporting.model.BillingReport;
import com.university.parking.reporting.model.UsageReport;
import com.university.parking.reporting.repository.BillingReportRepository;
import com.university.parking.reporting.repository.EntryReportRepository;
import com.university.parking.reporting.repository.ExitReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportingServiceTest {

    @Mock
    private EntryReportRepository entryRepo;

    @Mock
    private ExitReportRepository exitRepo;

    @Mock
    private BillingReportRepository billingRepo;

    @InjectMocks
    private ReportingService reportingService;

    @Test
    void generateReport_WithBillingData_ShouldCalculateRevenue() {
        // Arrange
        when(entryRepo.count()).thenReturn(20L);
        when(exitRepo.count()).thenReturn(15L);
        
        // El servicio primero llama a count()
        when(billingRepo.count()).thenReturn(2L);
        
        // Luego llama a findAll() si count > 0
        BillingReport bill1 = new BillingReport("ABC123", 25.75);
        BillingReport bill2 = new BillingReport("XYZ789", 15.50);
        List<BillingReport> bills = Arrays.asList(bill1, bill2);
        when(billingRepo.findAll()).thenReturn(bills);

        // Act
        UsageReport report = reportingService.generateReport();

        // Assert
        assertNotNull(report);
        assertEquals(20L, report.getTotalEntries());
        assertEquals(15L, report.getTotalExits());
        assertEquals(5L, report.getActiveVehicles()); // 20 - 15
        assertEquals(41.25, report.getTotalRevenue(), 0.01); // 25.75 + 15.50
        
        verify(entryRepo, times(1)).count();
        verify(exitRepo, times(1)).count();
        verify(billingRepo, times(1)).count();
        verify(billingRepo, times(1)).findAll();
    }

    @Test
    void generateReport_NoBillingData_ShouldReturnZeroRevenue() {
        // Arrange
        when(entryRepo.count()).thenReturn(10L);
        when(exitRepo.count()).thenReturn(8L);
        
        // Sin datos de billing
        when(billingRepo.count()).thenReturn(0L);
        // No debería llamar a findAll() si count == 0

        // Act
        UsageReport report = reportingService.generateReport();

        // Assert
        assertEquals(10L, report.getTotalEntries());
        assertEquals(8L, report.getTotalExits());
        assertEquals(2L, report.getActiveVehicles()); // 10 - 8
        assertEquals(0.0, report.getTotalRevenue(), 0.01);
        
        verify(entryRepo, times(1)).count();
        verify(exitRepo, times(1)).count();
        verify(billingRepo, times(1)).count();
        verify(billingRepo, never()).findAll(); // No debería llamarse
    }

    @Test
    void registerEntry_ShouldSaveWithCurrentTimeIfNull() {
        // Arrange
        String plate = "TEST123";
        
        // Act
        reportingService.registerEntry(plate, null);
        
        // Assert
        verify(entryRepo, times(1)).save(any());
    }

    @Test
    void registerExit_ShouldSaveWithCurrentTimeIfNull() {
        // Arrange
        String plate = "TEST123";
        
        // Act
        reportingService.registerExit(plate, null);
        
        // Assert
        verify(exitRepo, times(1)).save(any());
    }

    @Test
    void registerBilling_ShouldSaveBillingReport() {
        // Arrange
        String plate = "BILL456";
        double amount = 50.25;
        
        // Act
        reportingService.registerBilling(plate, amount);
        
        // Assert
        verify(billingRepo, times(1)).save(any(BillingReport.class));
    }
}