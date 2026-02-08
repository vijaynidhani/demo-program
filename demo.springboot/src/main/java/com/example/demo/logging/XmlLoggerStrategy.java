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
            Map<String, Object> m = new HashMap<>();
            m.put("timestamp", Instant.now().toString());
            m.put("level", level);
            m.put("message", message);
            m.put("payload", payload);
            return xmlMapper.writeValueAsString(m);
        } catch (Exception e) {
            return "<log><level>" + level + "</level><message>" + message + "</message></log>";
        }
    }
}
