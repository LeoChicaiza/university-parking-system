package com.university.parking.reporting.repository;

import com.university.parking.reporting.model.BillingReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BillingReportRepository extends JpaRepository<BillingReport, UUID> {
}
