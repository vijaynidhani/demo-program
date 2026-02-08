package com.example.hellospringboot.payment.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Order(1)
public class IndiaChargingStrategy implements ChargingStrategy {

    private static final BigDecimal GST_RATE = new BigDecimal("0.18"); // 18% GST
    private static final BigDecimal SERVICE_CHARGE = new BigDecimal("10.0"); // Fixed service charge

    @Override
    public BigDecimal calculateCharges(BigDecimal baseAmount, String country) {
        // Add 18% GST + 10 INR service charge
        BigDecimal gst = baseAmount.multiply(GST_RATE).setScale(2, RoundingMode.HALF_UP);
        return baseAmount.add(gst).add(SERVICE_CHARGE);
    }

    @Override
    public boolean supports(String country) {
        return country != null && country.equalsIgnoreCase("IN");
    }
}
