package com.example.hellospringboot.payment;

import java.math.BigDecimal;

public class PaymentDTO {
    private BigDecimal amount;
    private String toAccount;
    private String fromAccount;
    private String name;
    private String description;

    public PaymentDTO() {}

    public PaymentDTO(BigDecimal amount, String toAccount, String fromAccount, String name) {
        this.amount = amount;
        this.toAccount = toAccount;
        this.fromAccount = fromAccount;
        this.name = name;
    }

    public PaymentDTO(BigDecimal amount, String toAccount, String fromAccount, String name, String description) {
        this.amount = amount;
        this.toAccount = toAccount;
        this.fromAccount = fromAccount;
        this.name = name;
        this.description = description;
    }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }

    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
