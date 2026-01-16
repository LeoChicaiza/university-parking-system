package com.university.parking.exit.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;  // CORRECTO: @Configuration

@Configuration  // CORRECTO: @Configuration
public class RabbitConfig {

    public static final String EXIT_QUEUE = "vehicle.exit.queue";

    @Bean
    public Queue exitQueue() {
        return new Queue(EXIT_QUEUE, true);
    }
}