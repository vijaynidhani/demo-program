package com.example.demo;

import com.example.demo.logging.LoggerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    private final LoggerService loggerService;

    public HelloController(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(defaultValue = "world") String name) {
        loggerService.log("INFO", "Hello endpoint called with name: " + name);
        return "Hello, " + name + "!";
    }
}
