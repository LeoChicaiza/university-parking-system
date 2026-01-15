package com.university.parking.parkingspace.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ExitRabbitConsumer {

    @RabbitListener(queues = RabbitConfig.EXIT_QUEUE)
    public void handleVehicleExit(String message) {
        System.out.println("📩 Parking-Space received exit message: " + message);
    }
}
