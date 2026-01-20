package com.university.parking.exit.service;

import com.university.parking.exit.kafka.event.VehicleExitEvent;
import com.university.parking.exit.kafka.producer.ExitEventProducer;
import com.university.parking.exit.rabbitmq.ExitRabbitProducer;
import com.university.parking.exit.logging.SupabaseLogClient;
import com.university.parking.exit.model.ParkingExit;
import com.university.parking.exit.repository.ExitRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class ExitService {

    private static final String ACTIVE_ENTRY_KEY = "active-entry:";

    private final ExitRepository repository;
    private final ExitEventProducer kafkaProducer;
    private final ExitRabbitProducer rabbitProducer;
    private final RestTemplate restTemplate;
    private final StringRedisTemplate redisTemplate;
    private final SupabaseLogClient logger;

    public ExitService(
            ExitRepository repository,
            ExitEventProducer kafkaProducer,
            ExitRabbitProducer rabbitProducer,
            RestTemplate restTemplate,
            StringRedisTemplate redisTemplate,
            SupabaseLogClient logger
    ) {
        this.repository = repository;
        this.kafkaProducer = kafkaProducer;
        this.rabbitProducer = rabbitProducer;
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
        this.logger = logger;
    }

    public Map<String, Object> processExit(String plate) {

        logger.info("Processing vehicle exit", Map.of("plate", plate));

        // 1️⃣ CONSULTAR ENTRADA ACTIVA
        Map entry = restTemplate.getForObject(
                "http://entry-service/entry/active/{plate}",
                Map.class,
                plate
        );

        if (entry == null || entry.get("id") == null) {
            logger.error("No active entry found for exit", Map.of("plate", plate));
            throw new RuntimeException("No active entry found for vehicle");
        }

        UUID entryId = UUID.fromString((String) entry.get("id"));
        Long exitTime = System.currentTimeMillis();

        // 2️⃣ PERSISTIR SALIDA
        ParkingExit exit = new ParkingExit(
                plate,
                entryId,
                exitTime
        );

        ParkingExit saved = repository.save(exit);

        logger.info("Exit persisted successfully", Map.of(
                "exitId", saved.getId(),
                "plate", plate
        ));

        // 3️⃣ EVENTO KAFKA
        kafkaProducer.sendVehicleExit(
                new VehicleExitEvent(
                        plate,
                        LocalDateTime.now().toString()
                )
        );

        // 4️⃣ MENSAJE RABBITMQ
        rabbitProducer.sendExitMessage(plate);

        // 5️⃣ LIMPIAR REDIS
        redisTemplate.delete(ACTIVE_ENTRY_KEY + plate);

        logger.info("Vehicle exit completed", Map.of(
                "plate", plate,
                "exitTime", exitTime
        ));

        return Map.of(
                "exitId", saved.getId(),
                "plate", plate,
                "status", "EXIT_REGISTERED",
                "exitTime", exitTime
        );
    }
}
