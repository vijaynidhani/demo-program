package com.example.hellospringboot.payment.processor;

import com.example.hellospringboot.payment.PaymentDTO;
import com.example.hellospringboot.payment.PaymentRequest;
import com.example.hellospringboot.payment.PaymentService;
import com.example.hellospringboot.logging.LoggerService;
import org.springframework.stereotype.Service;

@Service
public class DefaultPaymentProcessor extends PaymentProcessorTemplate {

    public DefaultPaymentProcessor(PaymentService paymentService, LoggerService loggerService) {
        super(paymentService, loggerService);
    }

    @Override
    protected void beforeProcess(PaymentRequest request, String idempotencyKey) {
        super.beforeProcess(request, idempotencyKey);
        // additional hooks can be added here (e.g., enrich request)
    }

    @Override
    protected PaymentDTO doProcess(PaymentRequest request, String idempotencyKey) {
        // could add rate-limiting, charging, etc. here before delegating
        return super.doProcess(request, idempotencyKey);
    }

    @Override
    protected void afterProcess(PaymentRequest request, PaymentDTO result, String idempotencyKey) {
        super.afterProcess(request, result, idempotencyKey);
        // post-processing hooks (notifications, metrics)
    }
}
