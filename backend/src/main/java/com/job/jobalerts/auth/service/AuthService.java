package com.job.jobalerts.auth.service;

import com.job.jobalerts.auth.dto.AuthResponse;
import com.job.jobalerts.auth.dto.SendOtpRequest;
import com.job.jobalerts.auth.dto.VerifyOtpRequest;
import com.job.jobalerts.auth.entity.OtpVerification;
import com.job.jobalerts.auth.entity.User;
import com.job.jobalerts.auth.repository.OtpRepository;
import com.job.jobalerts.auth.repository.UserRepository;
import com.job.jobalerts.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final EmailService emailService;

    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int OTP_LENGTH = 6;

    @Transactional
    public AuthResponse sendOtp(SendOtpRequest request) {
        String email = request.getEmail();
        log.info("Sending OTP to: {}", email);

        // Delete any existing OTP for this email
        otpRepository.deleteByEmail(email);

        // Generate OTP
        String otp = RandomStringUtils.randomNumeric(OTP_LENGTH);
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);

        // Save OTP to database
        OtpVerification otpEntity = OtpVerification.builder()
                .email(email)
                .otp(otp)
                .expiresAt(expiresAt)
                .build();
        otpRepository.save(otpEntity);

        // Send OTP via email
        emailService.sendOtpEmail(email, otp);

        return AuthResponse.builder()
                .success(true)
                .message("OTP sent successfully")
                .email(email)
                .build();
    }

    @Transactional
    public AuthResponse verifyOtp(VerifyOtpRequest request) {
        String email = request.getEmail();
        String otp = request.getOtp();
        log.info("Verifying OTP for: {}", email);

        // Find valid OTP
        OtpVerification otpEntity = otpRepository
                .findByEmailAndOtpAndVerifiedFalse(email, otp)
                .orElseThrow(() -> new RuntimeException("Invalid or expired OTP"));

        // Check if OTP is expired
        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        // Mark OTP as verified
        otpEntity.setVerified(true);
        otpRepository.save(otpEntity);

        // Create or update user
        User user = userRepository.findByEmail(email)
                .orElse(User.builder().email(email).build());

        user.setVerified(true);
        user.setSubscribed(true);
        userRepository.save(user);

        // Send welcome email
        emailService.sendWelcomeEmail(email);

        return AuthResponse.builder()
                .success(true)
                .message("OTP verified successfully")
                .email(email)
                .build();
    }

    @Transactional
    public AuthResponse unsubscribe(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setSubscribed(false);
        userRepository.save(user);

        return AuthResponse.builder()
                .success(true)
                .message("Unsubscribed successfully")
                .email(email)
                .build();
    }
}