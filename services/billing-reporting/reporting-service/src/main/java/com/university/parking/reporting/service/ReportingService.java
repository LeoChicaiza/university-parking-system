
package com.university.parking.reporting.service;

import com.university.parking.reporting.model.UsageReport;
import org.springframework.stereotype.Service;

@Service
public class ReportingService {

    public UsageReport generateReport() {

        // Simulated aggregated data
        int entries = 120;
        int exits = 115;
        double revenue = 320.50;

        return new UsageReport(entries, exits, revenue);
    }
}
