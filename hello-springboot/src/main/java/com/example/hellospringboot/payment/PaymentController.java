package com.example.hellospringboot.payment;

import com.example.hellospringboot.logging.LoggerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final LoggerService loggerService;

    public PaymentController(PaymentService paymentService, LoggerService loggerService) {
        this.paymentService = paymentService;
        this.loggerService = loggerService;
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentRequest request,
                                                    @org.springframework.web.bind.annotation.RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        // Basic null checks
        if (request.getAmount() == null || request.getToAccount() == null || request.getFromAccount() == null) {
            loggerService.log("WARN", "Invalid payment request", request);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        PaymentDTO dto;
        try {
            dto = paymentService.process(request, idempotencyKey);
        } catch (IllegalArgumentException e) {
            loggerService.log("ERROR", "No adapter for payment", request);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        loggerService.log("INFO", "Processed payment", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
