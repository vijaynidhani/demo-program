package com.example.hellospringboot.payment;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SampleDataLoader implements CommandLineRunner {

    private final PaymentService paymentService;

    public SampleDataLoader(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed a few sample payments (idempotency keys used for demonstration)
        PaymentRequest p1 = new PaymentRequest();
        p1.setAmount(new BigDecimal("25.50"));
        p1.setToAccount("alice@upi");
        p1.setFromAccount("100200300");
        p1.setName("Alice Seed");
        p1.setMethod("upi");
        paymentService.process(p1, "seed-upi-1");

        PaymentRequest p2 = new PaymentRequest();
        p2.setAmount(new BigDecimal("75.00"));
        p2.setToAccount("merchant");
        p2.setFromAccount("4111111111111111");
        p2.setName("Card Seed");
        p2.setMethod("card");
        paymentService.process(p2, "seed-card-1");

        PaymentRequest p3 = new PaymentRequest();
        p3.setAmount(new BigDecimal("5.25"));
        p3.setToAccount("charlie@upi");
        p3.setFromAccount("500600700");
        p3.setName("Charlie Seed");
        p3.setMethod("upi");
        paymentService.process(p3, "seed-upi-2");
    }
}
