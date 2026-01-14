package com.university.parking.reporting.repository;

import com.university.parking.reporting.model.EntryReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EntryReportRepository extends JpaRepository<EntryReport, UUID> {
}
