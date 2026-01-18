package com.university.parking.reporting.service;

import com.university.parking.reporting.model.*;
import com.university.parking.reporting.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportingService {

    private final EntryReportRepository entryRepo;
    private final ExitReportRepository exitRepo;
    private final BillingReportRepository billingRepo;

    public ReportingService(
            EntryReportRepository entryRepo,
            ExitReportRepository exitRepo,
            BillingReportRepository billingRepo
    ) {
        this.entryRepo = entryRepo;
        this.exitRepo = exitRepo;
        this.billingRepo = billingRepo;
    }

    public void registerEntry(String plate, LocalDateTime time) {
        EntryReport entryReport = new EntryReport(plate, time != null ? time : LocalDateTime.now());
        entryRepo.save(entryReport);
        System.out.println("📝 ENTRY REGISTERED: Plate " + plate + " at " + entryReport.getEntryTime());
    }

    public void registerExit(String plate, LocalDateTime time) {
        ExitReport exitReport = new ExitReport(plate, time != null ? time : LocalDateTime.now());
        exitRepo.save(exitReport);
        System.out.println("📝 EXIT REGISTERED: Plate " + plate + " at " + exitReport.getExitTime());
    }

    public void registerBilling(String plate, double amount) {
        BillingReport billingReport = new BillingReport(plate, amount);
        billingRepo.save(billingReport);
        System.out.println("💰 BILLING REGISTERED: Plate " + plate + " - Amount: $" + amount);
    }

    // Nuevo método para generar reporte de uso
    public UsageReport generateReport() {
        try {
            // Obtener conteo total de entradas
            long totalEntries = entryRepo.count();
            
            // Obtener conteo total de salidas
            long totalExits = exitRepo.count();
            
            // Calcular el revenue total sumando todos los montos de billing
            Double totalRevenue = 0.0;
            
            // Opción 1: Si tienes un método personalizado en el repositorio
            // totalRevenue = billingRepo.sumTotalAmount();
            
            // Opción 2: Usar stream para sumar (si no tienes método personalizado)
            if (billingRepo.count() > 0) {
                totalRevenue = billingRepo.findAll().stream()
                    .mapToDouble(BillingReport::getAmount)
                    .sum();
            }
            
            // Crear y retornar el reporte
            UsageReport report = new UsageReport(totalEntries, totalExits, totalRevenue);
            
            System.out.println("📊 REPORT GENERATED: " + report);
            return report;
            
        } catch (Exception e) {
            System.err.println("❌ ERROR generating report: " + e.getMessage());
            // En caso de error, retornar un reporte con ceros
            return new UsageReport(0, 0, 0.0);
        }
    }
}