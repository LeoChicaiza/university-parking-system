
package com.university.parking.notification.controller;

import com.university.parking.notification.model.NotificationRequest;
import com.university.parking.notification.model.NotificationResponse;
import com.university.parking.notification.service.NotificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping
    public NotificationResponse notify(@RequestBody NotificationRequest request) {
        boolean sent = service.send(request);
        return new NotificationResponse(sent ? "SENT" : "FAILED");
    }
}
