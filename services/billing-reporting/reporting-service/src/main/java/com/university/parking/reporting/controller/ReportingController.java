package com.university.parking.reporting.controller;

import com.university.parking.reporting.model.UsageReport;
import com.university.parking.reporting.service.ReportingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;  // IMPORT FALTANTE

@RestController
@RequestMapping("/reports")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/usage")
    public ResponseEntity<UsageReport> getUsageReport() {
        try {
            UsageReport report = reportingService.generateReport();
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            // Retornar un reporte vacío en caso de error
            return ResponseEntity.ok(new UsageReport(0, 0, 0.0));
        }
    }
    
    // Endpoints adicionales útiles (opcionales)
    @PostMapping("/entry")
    public ResponseEntity<String> registerEntry(
            @RequestParam String plate,
            @RequestParam(required = false) String timestamp) {
        
        try {
            reportingService.registerEntry(plate, 
                timestamp != null ? 
                LocalDateTime.parse(timestamp) : 
                LocalDateTime.now());
            return ResponseEntity.ok("Entry registered successfully for plate: " + plate);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Error registering entry: " + e.getMessage());
        }
    }
    
    @PostMapping("/exit")
    public ResponseEntity<String> registerExit(
            @RequestParam String plate,
            @RequestParam(required = false) String timestamp) {
        
        try {
            reportingService.registerExit(plate,
                timestamp != null ?
                LocalDateTime.parse(timestamp) :
                LocalDateTime.now());
            return ResponseEntity.ok("Exit registered successfully for plate: " + plate);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Error registering exit: " + e.getMessage());
        }
    }
    
    @PostMapping("/billing")
    public ResponseEntity<String> registerBilling(
            @RequestParam String plate,
            @RequestParam double amount) {
        
        try {
            reportingService.registerBilling(plate, amount);
            return ResponseEntity.ok("Billing registered successfully for plate: " + plate);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Error registering billing: " + e.getMessage());
        }
    }
}