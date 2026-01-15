package com.university.parking.billing.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "billing_record", schema = "access_domain")
public class BillingRecord {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String plate;

    @Column(nullable = false)
    private Long entryTime;

    @Column(nullable = false)
    private Long exitTime;

    @Column(nullable = false)
    private Long durationMinutes;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    private BillingStatus status;

    @Column(nullable = false)
    private Long createdAt;

    protected BillingRecord() {}

    public BillingRecord(
            String plate,
            Long entryTime,
            Long exitTime,
            Long durationMinutes,
            Double amount
    ) {
        this.plate = plate;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.durationMinutes = durationMinutes;
        this.amount = amount;
        this.status = BillingStatus.CREATED;
        this.createdAt = System.currentTimeMillis();
    }

    public UUID getId() { return id; }
    public String getPlate() { return plate; }
    public Long getEntryTime() { return entryTime; }
    public Long getExitTime() { return exitTime; }
    public Long getDurationMinutes() { return durationMinutes; }
    public Double getAmount() { return amount; }
    public BillingStatus getStatus() { return status; }
}

