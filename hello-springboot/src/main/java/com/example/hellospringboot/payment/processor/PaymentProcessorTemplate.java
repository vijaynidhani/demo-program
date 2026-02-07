package com.example.hellospringboot.payment.processor;

import com.example.hellospringboot.payment.PaymentDTO;
import com.example.hellospringboot.payment.PaymentRequest;
import com.example.hellospringboot.payment.PaymentService;
import com.example.hellospringboot.logging.LoggerService;

public abstract class PaymentProcessorTemplate {

    protected final PaymentService paymentService;
    protected final LoggerService loggerService;

    protected PaymentProcessorTemplate(PaymentService paymentService, LoggerService loggerService) {
        this.paymentService = paymentService;
        this.loggerService = loggerService;
    }

    public final PaymentDTO execute(PaymentRequest request, String idempotencyKey) {
        validate(request);
        beforeProcess(request, idempotencyKey);
        PaymentDTO result = doProcess(request, idempotencyKey);
        afterProcess(request, result, idempotencyKey);
        return result;
    }

    protected void validate(PaymentRequest request) {
        if (request == null) throw new IllegalArgumentException("request cannot be null");
        if (request.getAmount() == null) throw new IllegalArgumentException("amount required");
    }

    protected void beforeProcess(PaymentRequest request, String idempotencyKey) {
        loggerService.log("DEBUG", "Preparing to process payment", request);
    }

    protected PaymentDTO doProcess(PaymentRequest request, String idempotencyKey) {
        return paymentService.process(request, idempotencyKey);
    }

    protected void afterProcess(PaymentRequest request, PaymentDTO result, String idempotencyKey) {
        loggerService.log("INFO", "Completed processing payment", result);
    }
}
