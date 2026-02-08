package com.example.hellospringboot.payment.strategy;

import java.math.BigDecimal;

public interface ChargingStrategy {
    BigDecimal calculateCharges(BigDecimal baseAmount, String country);
    boolean supports(String country);
}
