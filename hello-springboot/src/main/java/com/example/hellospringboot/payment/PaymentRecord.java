package com.example.hellospringboot.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payment_records")
public class PaymentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant timestamp;

    @Column(unique = true)
    private String idempotencyKey;

    private BigDecimal amount;
    private String toAccount;
    private String fromAccount;
    private String name;

    public PaymentRecord() {}

    public PaymentRecord(String idempotencyKey, PaymentDTO dto) {
        this.timestamp = Instant.now();
        this.idempotencyKey = idempotencyKey;
        this.amount = dto.getAmount();
        this.toAccount = dto.getToAccount();
        this.fromAccount = dto.getFromAccount();
        this.name = dto.getName();
    }

    public Long getId() { return id; }
    public Instant getTimestamp() { return timestamp; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public BigDecimal getAmount() { return amount; }
    public String getToAccount() { return toAccount; }
    public String getFromAccount() { return fromAccount; }
    public String getName() { return name; }

    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }
    public void setName(String name) { this.name = name; }
}
