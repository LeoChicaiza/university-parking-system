package com.university.parking.exit.service;

import com.university.parking.exit.kafka.event.VehicleExitEvent;
import com.university.parking.exit.kafka.producer.ExitEventProducer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ExitService {

    private final ExitEventProducer producer;

    public ExitService(ExitEventProducer producer) {
        this.producer = producer;
    }

    /**
     * Registra la salida de un vehículo y emite un evento Kafka
     */
    public Map<String, Object> processExit(String plate) {

        // 1️⃣ Emitir evento de salida
        VehicleExitEvent event = new VehicleExitEvent(
                plate,
                LocalDateTime.now().toString()
        );

        producer.sendVehicleExit(event);

        // 2️⃣ Respuesta inmediata (no síncrona)
        return Map.of(
                "plate", plate,
                "status", "EXIT_REGISTERED",
                "timestamp", event.getExitTime()
        );
    }
}
