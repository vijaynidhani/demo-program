package com.example.hellospringboot.payment;

import java.time.Instant;
import java.util.UUID;

public class PaymentRecord {
    private final String id;
    private final Instant timestamp;
    private final String idempotencyKey;
    private final PaymentDTO payment;

    public PaymentRecord(String idempotencyKey, PaymentDTO payment) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
        this.idempotencyKey = idempotencyKey;
        this.payment = payment;
    }

    public String getId() { return id; }
    public Instant getTimestamp() { return timestamp; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public PaymentDTO getPayment() { return payment; }
}
