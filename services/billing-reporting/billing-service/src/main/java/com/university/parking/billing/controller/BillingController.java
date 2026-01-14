package com.university.parking.billing.controller;

import com.university.parking.billing.model.BillingRecord;
import com.university.parking.billing.model.BillingRequest;
import com.university.parking.billing.service.BillingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/billing")
public class BillingController {

    private final BillingService service;

    public BillingController(BillingService service) {
        this.service = service;
    }

    @PostMapping("/calculate")
    public BillingRecord calculate(@RequestBody BillingRequest request) {
        return service.calculate(request);
    }
}
