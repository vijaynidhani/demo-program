package com.example.demo.logging;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class XmlLoggerStrategy implements LoggerStrategy {

    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public boolean supports(String format) {
        return "xml".equalsIgnoreCase(format);
    }

    @Override
    public String format(String level, String message, Object payload) {
        try {
            Map<String, Object> logData = new HashMap<>();
            logData.put("timestamp", Instant.now().toString());
            logData.put("level", level);
            logData.put("message", message);
            logData.put("payload", payload);
            return xmlMapper.writeValueAsString(logData);
        } catch (Exception e) {
            // Fallback with proper escaping
            String escapedLevel = escapeXml(level);
            String escapedMessage = escapeXml(message);
            return "<log><level>" + escapedLevel + "</level><message>" + escapedMessage + "</message></log>";
        }
    }
    
    private String escapeXml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }
}
