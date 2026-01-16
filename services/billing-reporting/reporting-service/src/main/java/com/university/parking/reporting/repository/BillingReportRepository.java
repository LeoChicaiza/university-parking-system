package com.university.parking.reporting.repository;

import com.university.parking.reporting.model.BillingReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BillingReportRepository extends JpaRepository<BillingReport, UUID> {
    
    // Método para obtener la suma total de todos los montos
    @Query("SELECT SUM(b.amount) FROM BillingReport b")
    Double sumTotalAmount();
    
    // Método para contar facturaciones por placa
    Long countByPlate(String plate);
    
    // Método para obtener el total facturado por placa
    @Query("SELECT SUM(b.amount) FROM BillingReport b WHERE b.plate = :plate")
    Double sumAmountByPlate(String plate);
}