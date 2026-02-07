package com.example.hellospringboot.controller;

import com.example.hellospringboot.logging.LoggerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final LoggerService loggerService;

    public HelloController(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    @GetMapping("/hello")
    public String hello() {
        loggerService.log("INFO", "Hello endpoint called");
        return "Hello, world!";
    }
}
