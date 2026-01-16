package com.university.parking.notification.service;

import com.university.parking.notification.model.Notification;
import com.university.parking.notification.model.NotificationRequest;
import com.university.parking.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void createBillingNotification(String plate, double amount) {
        String message = "Billing generated for vehicle " + plate +
                         " with amount $" + amount;

        Notification notification = new Notification(plate, amount, message);
        repository.save(notification);

        System.out.println("📨 NOTIFICATION SENT: " + message);
    }

    // Método requerido por NotificationController
    public boolean send(NotificationRequest request) {
        if (request == null) {
            return false;
        }
        
        String plate = extractPlateFromMessage(request.getMessage());
        double amount = extractAmountFromMessage(request.getMessage());
        
        String customMessage = String.format("[%s] User %s: %s", 
            request.getType() != null ? request.getType() : "UNKNOWN", 
            request.getUserId() != null ? request.getUserId() : "UNKNOWN", 
            request.getMessage() != null ? request.getMessage() : "");
        
        Notification notification = new Notification(plate, amount, customMessage);
        repository.save(notification);
        
        System.out.println("📨 NOTIFICATION SENT: " + customMessage);
        return true;
    }
    
    // Métodos auxiliares privados - CORREGIDOS
    private String extractPlateFromMessage(String message) {
        if (message == null || message.isEmpty()) {
            return "UNKNOWN";
        }
        
        try {
            // Buscar patrón de placa después de "Vehicle" (ignorando mayúsculas/minúsculas)
            Pattern vehiclePattern = Pattern.compile("(?:vehicle|plate)\\s+([A-Z]{3}[0-9]{3})", 
                                                     Pattern.CASE_INSENSITIVE);
            java.util.regex.Matcher matcher = vehiclePattern.matcher(message);
            
            if (matcher.find()) {
                return matcher.group(1).toUpperCase();
            }
            
            // Si no encuentra con "Vehicle", buscar cualquier patrón de placa (3 letras + 3 números)
            Pattern platePattern = Pattern.compile("\\b([A-Z]{3}[0-9]{3})\\b");
            matcher = platePattern.matcher(message);
            
            if (matcher.find()) {
                return matcher.group(1);
            }
            
        } catch (Exception e) {
            // Si hay error, retornar UNKNOWN
        }
        
        return "UNKNOWN";
    }
    
    private double extractAmountFromMessage(String message) {
        if (message == null || message.isEmpty()) {
            return 0.0;
        }
        
        try {
            // Primero buscar montos con signo de dólar: $15.50, billed $25, etc.
            Pattern amountPattern = Pattern.compile("(?:billed\\s*)?\\$\\s*(\\d+(?:\\.\\d{1,2})?)", 
                                                    Pattern.CASE_INSENSITIVE);
            java.util.regex.Matcher matcher = amountPattern.matcher(message);
            
            if (matcher.find()) {
                String amountStr = matcher.group(1);
                return Double.parseDouble(amountStr);
            }
            
            // Si no encuentra con $, buscar montos después de "billed" o "amount"
            Pattern billedPattern = Pattern.compile("(?:billed|amount)\\s+(\\d+(?:\\.\\d{1,2})?)", 
                                                    Pattern.CASE_INSENSITIVE);
            matcher = billedPattern.matcher(message);
            
            if (matcher.find()) {
                String amountStr = matcher.group(1);
                return Double.parseDouble(amountStr);
            }
            
        } catch (Exception e) {
            // Si hay error de parseo, retornar 0.0
        }
        
        return 0.0;
    }
}