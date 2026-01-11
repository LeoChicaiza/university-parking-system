
package com.university.parking.billing.controller;

import com.university.parking.billing.model.BillingRequest;
import com.university.parking.billing.model.BillingResponse;
import com.university.parking.billing.service.BillingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping
    public BillingResponse generate(@RequestBody BillingRequest request) {
        return billingService.calculate(request.getMinutesParked());
    }
}
