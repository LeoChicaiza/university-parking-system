package com.university.parking.billing.repository;

import com.university.parking.billing.model.BillingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BillingRepository extends JpaRepository<BillingRecord, UUID> {
}

