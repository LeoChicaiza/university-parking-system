package com.university.parking.lot.rabbitmq;

import com.university.parking.lot.service.ParkingLotService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ExitRabbitConsumer {

    private final ParkingLotService parkingLotService;

    public ExitRabbitConsumer(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    /**
     * Consume mensaje de salida y libera capacidad del parqueadero
     *
     * Mensaje esperado:
     * "Vehicle exited: <LOT_ID>"
     */
    @RabbitListener(queues = RabbitConfig.EXIT_QUEUE)
    public void handleVehicleExit(String message) {

        System.out.println("📩 Parking-Lot received: " + message);

        String lotId = message.replace("Vehicle exited:", "").trim();

        parkingLotService.release(lotId);

        System.out.println("✅ Parking lot capacity released: " + lotId);
    }
}

