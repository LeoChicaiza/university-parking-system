package com.university.parking.reporting.repository;

import com.university.parking.reporting.model.ExitReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExitReportRepository extends JpaRepository<ExitReport, UUID> {
}
