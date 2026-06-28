package com.job.jobalerts.email.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class BrevoApiEmailService {

    @Value("${BREVO_API_KEY:${BREVO_SMTP_KEY}}")  // Fallback to SMTP key if API key not found
    private String apiKey;

    @Value("${BREVO_FROM_EMAIL:${BREVO_USERNAME}}")  // Use BREVO_FROM_EMAIL, fallback to BREVO_USERNAME
    private String fromEmail;

    @Value("${BREVO_FROM_NAME:Job Alerts}")  // Use BREVO_FROM_NAME, default to "Job Alerts"
    private String fromName;

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        log.info("========== BREVO API EMAIL SERVICE INITIALIZED (REST) ==========");
        log.info("From Email: {}", fromEmail);
        log.info("From Name: {}", fromName);
        if (apiKey != null && !apiKey.isEmpty()) {
            String maskedKey = apiKey.substring(0, Math.min(15, apiKey.length())) + "****";
            log.info("API Key: {}", maskedKey);
        } else {
            log.warn("API Key is empty or null!");
        }
        log.info("================================================================");
    }

    public void sendOtpEmail(String toEmail, String otp) {
        log.info("📧 Sending OTP via Brevo API (REST) to: {}", toEmail);

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

            sendEmail(toEmail, subject, htmlContent);

        } catch (Exception e) {
            log.error("❌ Failed to send OTP email to: {}", toEmail, e);
            System.out.println("=========================================");
            System.out.println("📧 BREVO API FAILED - Using Console OTP");
            System.out.println("OTP for " + toEmail + ": " + otp);
            System.out.println("Error: " + e.getMessage());
            System.out.println("=========================================");
        }
    }

    public void sendWelcomeEmail(String toEmail) {
        log.info("📧 Sending welcome email via Brevo API (REST) to: {}", toEmail);

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

        sendEmail(toEmail, subject, htmlContent);
    }

    public void sendEmail(String toEmail, String subject, String htmlContent) {
        try {
            // Validate API key
            if (apiKey == null || apiKey.isEmpty()) {
                log.error("❌ API Key is null or empty! Please check your configuration.");
                System.out.println("=========================================");
                System.out.println("📧 BREVO API FAILED - API Key Missing");
                System.out.println("Please add BREVO_API_KEY to your .env file");
                System.out.println("=========================================");
                return;
            }

            // Validate from email
            if (fromEmail == null || fromEmail.isEmpty()) {
                log.error("❌ From Email is null or empty! Please check your configuration.");
                System.out.println("=========================================");
                System.out.println("📧 BREVO API FAILED - From Email Missing");
                System.out.println("Please add BREVO_FROM_EMAIL to your .env file");
                System.out.println("=========================================");
                return;
            }

            // Build the request body
            Map<String, Object> requestBody = new LinkedHashMap<>();

            // Sender
            Map<String, String> sender = new LinkedHashMap<>();
            sender.put("name", fromName);
            sender.put("email", fromEmail);
            requestBody.put("sender", sender);

            // To
            List<Map<String, String>> toList = new ArrayList<>();
            Map<String, String> to = new LinkedHashMap<>();
            to.put("email", toEmail);
            toList.add(to);
            requestBody.put("to", toList);

            // Subject and content
            requestBody.put("subject", subject);
            requestBody.put("htmlContent", htmlContent);

            // Convert to JSON
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            log.debug("Request Body: {}", jsonBody);

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            // Send request
            ResponseEntity<String> response = restTemplate.exchange(
                    BREVO_API_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("✅ Email sent successfully to: {}", toEmail);
                log.debug("Response: {}", response.getBody());
            } else {
                log.error("❌ Failed to send email to: {}. Status: {}", toEmail, response.getStatusCode());
                log.error("Response: {}", response.getBody());
            }

        } catch (Exception e) {
            log.error("❌ Failed to send email to: {}", toEmail, e);
            System.out.println("=========================================");
            System.out.println("📧 EMAIL FAILED - Using Console Output");
            System.out.println("To: " + toEmail);
            System.out.println("Subject: " + subject);
            System.out.println("Error: " + e.getMessage());
            System.out.println("=========================================");
        }
    }
}