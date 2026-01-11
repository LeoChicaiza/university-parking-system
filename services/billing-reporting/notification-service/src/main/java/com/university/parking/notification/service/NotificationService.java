
package com.university.parking.notification.service;

import com.university.parking.notification.model.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public boolean send(NotificationRequest request) {

        if (request.getUserId() == null || request.getMessage() == null) {
            return false;
        }

        // Simulated notification sending
        System.out.println("Notification sent to user " + request.getUserId());
        System.out.println("Type: " + request.getType());
        System.out.println("Message: " + request.getMessage());

        return true;
    }
}
