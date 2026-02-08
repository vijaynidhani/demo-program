package com.example.hellospringboot.payment.processor;

import com.example.hellospringboot.payment.PaymentDTO;
import com.example.hellospringboot.payment.PaymentRequest;
import com.example.hellospringboot.payment.PaymentService;
import com.example.hellospringboot.payment.strategy.ChargingStrategy;
import com.example.hellospringboot.logging.LoggerService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Primary
public class ChargeablePaymentProcessor extends PaymentProcessorTemplate {

    private final List<ChargingStrategy> chargingStrategies;

    public ChargeablePaymentProcessor(PaymentService paymentService, 
                                     LoggerService loggerService,
                                     List<ChargingStrategy> chargingStrategies) {
        super(paymentService, loggerService);
        this.chargingStrategies = chargingStrategies;
    }

    @Override
    protected void beforeProcess(PaymentRequest request, String idempotencyKey) {
        super.beforeProcess(request, idempotencyKey);
        
        // Apply country-based charging strategy
        String country = request.getCountry();
        if (country != null && request.getAmount() != null) {
            BigDecimal originalAmount = request.getAmount();
            BigDecimal chargedAmount = applyChargingStrategy(originalAmount, country);
            
            if (chargedAmount.compareTo(originalAmount) != 0) {
                loggerService.log("INFO", "Applied country-based charges: " + country + 
                                         " Original: " + originalAmount + 
                                         " Charged: " + chargedAmount, request);
                request.setAmount(chargedAmount);
            }
        }
    }

    private BigDecimal applyChargingStrategy(BigDecimal amount, String country) {
        for (ChargingStrategy strategy : chargingStrategies) {
            if (strategy.supports(country)) {
                return strategy.calculateCharges(amount, country);
            }
        }
        // This should not happen as DefaultChargingStrategy supports all
        return amount;
    }

    @Override
    protected PaymentDTO doProcess(PaymentRequest request, String idempotencyKey) {
        return super.doProcess(request, idempotencyKey);
    }

    @Override
    protected void afterProcess(PaymentRequest request, PaymentDTO result, String idempotencyKey) {
        super.afterProcess(request, result, idempotencyKey);
    }
}
