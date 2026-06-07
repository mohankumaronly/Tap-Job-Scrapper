package com.job.jobalerts.email.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.from.email:onboarding@resend.dev}")
    private String fromEmail;

    @Value("${resend.from.name:Job Alerts}")
    private String fromName;

    private Resend resend;

    @PostConstruct
    public void init() {
        this.resend = new Resend(apiKey);
        log.info("========== RESEND EMAIL SERVICE INITIALIZED ==========");
        log.info("From Email: {}", fromEmail);
        log.info("From Name: {}", fromName);
        log.info("======================================================");
    }

    public void sendOtpEmail(String toEmail, String otp) {
        log.info("Sending OTP to: {}", toEmail);

        try {
            String subject = "Your OTP for Job Alerts";
            String htmlContent = String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Job Alerts OTP</title>
                </head>
                <body style="font-family: Arial, sans-serif;">
                    <div style="max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;">
                        <h2 style="color: #4CAF50;">Job Alerts Subscription</h2>
                        <p>Your OTP for email verification is:</p>
                        <div style="background-color: #f4f4f4; padding: 15px; font-size: 32px; font-weight: bold; text-align: center;">
                            %s
                        </div>
                        <p>This OTP is valid for <strong>5 minutes</strong>.</p>
                        <p>If you didn't request this, please ignore this email.</p>
                        <hr>
                        <p style="color: #888; font-size: 12px;">Job Alerts - Find your dream job</p>
                    </div>
                </body>
                </html>
                """, otp);

            CreateEmailOptions email = CreateEmailOptions.builder()
                    .from(fromName + " <" + fromEmail + ">")
                    .to(toEmail)
                    .subject(subject)
                    .html(htmlContent)
                    .build();

            CreateEmailResponse response = resend.emails().send(email);
            log.info("✅ OTP email sent successfully to: {}. Message ID: {}", toEmail, response.getId());

        } catch (ResendException e) {
            log.error("Failed to send OTP email to: {}", toEmail, e);
            System.out.println("=========================================");
            System.out.println("📧 RESEND EMAIL FAILED - Using Console OTP");
            System.out.println("OTP for " + toEmail + ": " + otp);
            System.out.println("=========================================");
        }
    }

    public void sendWelcomeEmail(String toEmail) {
        log.info("Sending welcome email to: {}", toEmail);

        try {
            String subject = "Welcome to Job Alerts! 🎉";
            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Welcome to Job Alerts</title>
                </head>
                <body style="font-family: Arial, sans-serif;">
                    <div style="max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;">
                        <h2 style="color: #4CAF50;">Welcome to Job Alerts! 🎉</h2>
                        <p>Thank you for subscribing to Job Alerts.</p>
                        <p>You will now receive email notifications when new jobs are posted.</p>
                        <p>Best regards,<br>Job Alerts Team</p>
                    </div>
                </body>
                </html>
                """;

            CreateEmailOptions email = CreateEmailOptions.builder()
                    .from(fromName + " <" + fromEmail + ">")
                    .to(toEmail)
                    .subject(subject)
                    .html(htmlContent)
                    .build();

            CreateEmailResponse response = resend.emails().send(email);
            log.info("✅ Welcome email sent successfully to: {}. Message ID: {}", toEmail, response.getId());

        } catch (ResendException e) {
            log.error("Failed to send welcome email to: {}", toEmail, e);
        }
    }

    public void sendEmail(String toEmail, String subject, String htmlContent) {
        log.info("Sending email to: {}", toEmail);

        try {
            CreateEmailOptions email = CreateEmailOptions.builder()
                    .from(fromName + " <" + fromEmail + ">")
                    .to(toEmail)
                    .subject(subject)
                    .html(htmlContent)
                    .build();

            CreateEmailResponse response = resend.emails().send(email);
            log.info("✅ Email sent successfully to: {}. Message ID: {}", toEmail, response.getId());

        } catch (ResendException e) {
            log.error("Failed to send email to: {}", toEmail, e);
        }
    }
}