package com.example.hellospringboot.payment.adapters;

import com.example.hellospringboot.payment.PaymentDTO;
import com.example.hellospringboot.payment.PaymentRequest;
import com.example.hellospringboot.payment.PaymentAdapter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UpiPaymentAdapter implements PaymentAdapter {

    @Override
    public boolean supports(PaymentRequest req) {
        if (req.getMethod() != null) return "upi".equalsIgnoreCase(req.getMethod());
        // fallback: if toAccount contains '@' treat as UPI
        return req.getToAccount() != null && req.getToAccount().contains("@");
    }

    @Override
    public PaymentDTO process(PaymentRequest req) {
        // Simulate UPI processing: generate transaction id
        PaymentDTO dto = new PaymentDTO(req.getAmount(), req.getToAccount(), req.getFromAccount(), req.getName());
        // attach a pseudo transaction id in name (or add field in real impl)
        dto.setName(dto.getName() + " (upiTx:" + UUID.randomUUID().toString() + ")");
        return dto;
    }
}
