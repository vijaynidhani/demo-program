package com.example.demo.logging;

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
            Map<String, Object> logData = new HashMap<>();
            logData.put("timestamp", Instant.now().toString());
            logData.put("level", level);
            logData.put("message", message);
            logData.put("payload", payload);
            return mapper.writeValueAsString(logData);
        } catch (Exception e) {
            // Fallback with proper escaping
            String escapedLevel = escapeJson(level);
            String escapedMessage = escapeJson(message);
            return "{\"level\":\"" + escapedLevel + "\",\"message\":\"" + escapedMessage + "\"}";
        }
    }
    
    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
