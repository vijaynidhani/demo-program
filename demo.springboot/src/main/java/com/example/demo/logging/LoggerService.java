package com.example.demo.logging;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoggerService {

    private final List<LoggerStrategy> strategies;

    private LoggerStrategy active;

    @Value("${app.logging.format:text}")
    private String format;

    public LoggerService(List<LoggerStrategy> strategies) {
        this.strategies = strategies;
    }

    @PostConstruct
    public void init() {
        for (LoggerStrategy s : strategies) {
            if (s.supports(format)) {
                active = s;
                break;
            }
        }
        if (active == null) active = strategies.stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No logging strategies available"));
    }

    public void log(String level, String message) {
        log(level, message, null);
    }

    public void log(String level, String message, Object payload) {
        String out = active.format(level, message, payload);
        // Using System.out for demo purposes - in production, use SLF4J or Spring's logging framework
        System.out.println(out);
    }
}
