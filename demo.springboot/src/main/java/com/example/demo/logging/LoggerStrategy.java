package com.example.demo.logging;

public interface LoggerStrategy {
    boolean supports(String format);
    String format(String level, String message, Object payload);
}
