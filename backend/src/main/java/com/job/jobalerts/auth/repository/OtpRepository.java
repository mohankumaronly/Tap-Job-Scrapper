package com.job.jobalerts.auth.repository;

import com.job.jobalerts.auth.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification> findByEmailAndOtpAndVerifiedFalse(String email, String otp);
    void deleteByExpiresAtBefore(LocalDateTime now);
    void deleteByEmail(String email);
}