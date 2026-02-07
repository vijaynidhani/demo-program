package com.example.hellospringboot.payment;

public interface PaymentAdapter {
    boolean supports(PaymentRequest req);
    PaymentDTO process(PaymentRequest req);
}
