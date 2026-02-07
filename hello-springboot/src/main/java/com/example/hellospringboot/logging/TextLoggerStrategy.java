package com.example.hellospringboot.logging;

import org.springframework.stereotype.Component;

@Component
public class TextLoggerStrategy implements LoggerStrategy {

    @Override
    public boolean supports(String format) {
        return "text".equalsIgnoreCase(format);
    }

    @Override
    public String format(String level, String message, Object payload) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(level).append("] ").append(message);
        if (payload != null) sb.append(" - ").append(payload.toString());
        return sb.toString();
    }
}
