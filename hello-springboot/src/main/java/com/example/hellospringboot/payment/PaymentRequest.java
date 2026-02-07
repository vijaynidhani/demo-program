package com.example.hellospringboot.payment;

import java.math.BigDecimal;

public class PaymentRequest {
    private BigDecimal amount;
    private String toAccount;
    private String fromAccount;
    private String name;
    private String method; // optional: upi or card

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }

    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
}
