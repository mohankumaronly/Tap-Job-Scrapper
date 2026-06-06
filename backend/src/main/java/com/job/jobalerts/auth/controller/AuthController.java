package com.job.jobalerts.auth.controller;

import com.job.jobalerts.auth.dto.AuthResponse;
import com.job.jobalerts.auth.dto.SendOtpRequest;
import com.job.jobalerts.auth.dto.VerifyOtpRequest;
import com.job.jobalerts.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/send-otp")
    public ResponseEntity<AuthResponse> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        log.info("POST /api/auth/send-otp - Email: {}", request.getEmail());
        AuthResponse response = authService.sendOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        log.info("POST /api/auth/verify-otp - Email: {}", request.getEmail());
        AuthResponse response = authService.verifyOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<AuthResponse> unsubscribe(@RequestParam String email) {
        log.info("POST /api/auth/unsubscribe - Email: {}", email);
        AuthResponse response = authService.unsubscribe(email);
        return ResponseEntity.ok(response);
    }
}