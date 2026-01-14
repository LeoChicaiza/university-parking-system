package com.university.parking.space.mqtt.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.parking.space.mqtt.dto.ParkingSpaceSensorMessage;
import com.university.parking.space.service.ParkingSpaceService;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class ParkingSpaceMqttSubscriber {

    private final ParkingSpaceService parkingSpaceService;
    private final ObjectMapper mapper = new ObjectMapper();

    public ParkingSpaceMqttSubscriber(ParkingSpaceService parkingSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
    }

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handle(Message<String> message) throws Exception {

        ParkingSpaceSensorMessage sensorMessage =
                mapper.readValue(message.getPayload(), ParkingSpaceSensorMessage.class);

        if (sensorMessage.isOccupied()) {
            parkingSpaceService.markOccupied(sensorMessage.getSpaceId());
        } else {
            parkingSpaceService.releaseSpace(sensorMessage.getSpaceId());
        }
    }
}

