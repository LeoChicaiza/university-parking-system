package com.university.parking.billing.service;

import com.university.parking.billing.model.BillingRecord;
import com.university.parking.billing.repository.BillingRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class BillingService {

    private final BillingRepository repository;
    private final RestTemplate restTemplate;

    public BillingService(BillingRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public BillingRecord processBilling(String plate, Long exitTime) {

        // 1️⃣ Consultar entrada activa (ENTRY-SERVICE)
        Map entry = restTemplate.getForObject(
                "http://entry-service/entry/active/{plate}",
                Map.class,
                plate
        );

        if (entry == null || entry.get("entryTime") == null) {
            throw new RuntimeException("Entry not found for billing");
        }

        Long entryTime = ((Number) entry.get("entryTime")).longValue();

        // 2️⃣ Calcular duración en minutos
        long durationMinutes =
                Math.max(1, (exitTime - entryTime) / (1000 * 60));

        // 3️⃣ Calcular monto (regla simple académica)
        double amount = durationMinutes * 0.05; // $0.05 por minuto

        // 4️⃣ Persistir billing
        BillingRecord record = new BillingRecord(
                plate,
                entryTime,
                exitTime,
                durationMinutes,
                amount
        );

        return repository.save(record);
    }
}
