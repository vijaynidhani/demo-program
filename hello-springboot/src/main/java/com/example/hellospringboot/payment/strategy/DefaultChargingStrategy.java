package com.example.hellospringboot.payment.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class DefaultChargingStrategy implements ChargingStrategy {

    private static final BigDecimal DEFAULT_RATE = new BigDecimal("0.05"); // 5% default charge

    @Override
    public BigDecimal calculateCharges(BigDecimal baseAmount, String country) {
        // Add 5% default charge for unspecified or unsupported countries
        BigDecimal charge = baseAmount.multiply(DEFAULT_RATE).setScale(2, RoundingMode.HALF_UP);
        return baseAmount.add(charge);
    }

    @Override
    public boolean supports(String country) {
        // Default strategy supports all countries (fallback)
        return true;
    }
}
