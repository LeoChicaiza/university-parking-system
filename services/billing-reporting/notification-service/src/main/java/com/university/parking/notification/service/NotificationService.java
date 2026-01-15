package com.university.parking.notification.service;

import com.university.parking.notification.model.Notification;
import com.university.parking.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void createBillingNotification(String plate, double amount) {

        String message = "Billing generated for vehicle " + plate +
                         " with amount $" + amount;

        Notification notification =
                new Notification(plate, amount, message);

        repository.save(notification);

        // Simulación académica
        System.out.println("📨 NOTIFICATION SENT: " + message);
    }
}
