package com.example.hellospringboot.payment.adapters;

import com.example.hellospringboot.payment.PaymentDTO;
import com.example.hellospringboot.payment.PaymentRequest;
import com.example.hellospringboot.payment.PaymentAdapter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CardPaymentAdapter implements PaymentAdapter {

    @Override
    public boolean supports(PaymentRequest req) {
        if (req.getMethod() != null) return "card".equalsIgnoreCase(req.getMethod());
        // fallback: if name contains 'card' or fromAccount looks like card number
        return req.getFromAccount() != null && req.getFromAccount().matches("\\d{12,19}");
    }

    @Override
    public PaymentDTO process(PaymentRequest req) {
        // Simulate card processing
        PaymentDTO dto = new PaymentDTO(req.getAmount(), req.getToAccount(), req.getFromAccount(), req.getName());
        dto.setName(dto.getName() + " (cardTx:" + UUID.randomUUID().toString() + ")");
        return dto;
    }
}
