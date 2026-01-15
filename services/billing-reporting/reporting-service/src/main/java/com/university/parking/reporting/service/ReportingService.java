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
        entryRepo.save(new EntryReport(plate, time));
    }

    public void registerExit(String plate, LocalDateTime time) {
        exitRepo.save(new ExitReport(plate, time));
    }

    public void registerBilling(String plate, double amount) {
        billingRepo.save(new BillingReport(plate, amount));
    }
}
