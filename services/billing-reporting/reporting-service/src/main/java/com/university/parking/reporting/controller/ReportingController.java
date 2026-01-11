
package com.university.parking.reporting.controller;

import com.university.parking.reporting.model.UsageReport;
import com.university.parking.reporting.service.ReportingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/usage")
    public UsageReport getUsageReport() {
        return reportingService.generateReport();
    }
}
