package com.job.jobalerts.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("service", "Job Alerts API");
        response.put("version", "1.0.0");
        return response;
    }

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
                "message", "Job Alerts API is running!",
                "status", "OK",
                "endpoints", "/api/jobs, /api/auth/send-otp, /api/auth/verify-otp"
        );
    }
}