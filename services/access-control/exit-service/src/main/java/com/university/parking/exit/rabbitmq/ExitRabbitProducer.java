package com.university.parking.exit.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExitRabbitProducer {

    private final RabbitTemplate rabbitTemplate;

    public ExitRabbitProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendExitMessage(String plate) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXIT_QUEUE,
                "Vehicle exited: " + plate
        );
    }
}
