package com.example.hellospringboot.logging;

public interface LoggerStrategy {
    boolean supports(String format);
    String format(String level, String message, Object payload);
}
