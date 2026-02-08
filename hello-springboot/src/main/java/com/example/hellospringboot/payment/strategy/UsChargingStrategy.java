package com.example.hellospringboot.payment.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Order(2)
public class UsChargingStrategy implements ChargingStrategy {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.08"); // 8% tax
    private static final BigDecimal PROCESSING_FEE = new BigDecimal("2.5"); // $2.5 processing fee

    @Override
    public BigDecimal calculateCharges(BigDecimal baseAmount, String country) {
        // Add 8% tax + $2.5 processing fee
        BigDecimal tax = baseAmount.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        return baseAmount.add(tax).add(PROCESSING_FEE);
    }

    @Override
    public boolean supports(String country) {
        return country != null && country.equalsIgnoreCase("US");
    }
}
