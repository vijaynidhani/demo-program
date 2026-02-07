package com.example.hellospringboot.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class JsonLoggerStrategy implements LoggerStrategy {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean supports(String format) {
        return "json".equalsIgnoreCase(format);
    }

    @Override
    public String format(String level, String message, Object payload) {
        try {
            Map<String, Object> m = new HashMap<>();
            m.put("timestamp", Instant.now().toString());
            m.put("level", level);
            m.put("message", message);
            m.put("payload", payload);
            return mapper.writeValueAsString(m);
        } catch (Exception e) {
            return "{\"level\":\"" + level + "\",\"message\":\"" + message + "\"}";
        }
    }
}
