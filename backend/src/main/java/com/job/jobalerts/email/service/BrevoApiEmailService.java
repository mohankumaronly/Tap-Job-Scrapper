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

    @Value("${BREVO_API_KEY:${BREVO_SMTP_KEY}}")
    private String apiKey;

    @Value("${BREVO_FROM_EMAIL:${BREVO_USERNAME}}")
    private String fromEmail;

    @Value("${BREVO_FROM_NAME:JobAlert}")
    private String fromName;

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        log.info("========== JOBALERT BREVO API SERVICE INITIALIZED ==========");
        log.info("From Email: {}", fromEmail);
        log.info("From Name: {}", fromName);
        if (apiKey != null && !apiKey.isEmpty()) {
            String maskedKey = apiKey.substring(0, Math.min(15, apiKey.length())) + "****";
            log.info("API Key: {}", maskedKey);
        } else {
            log.warn("API Key is empty or null!");
        }
        log.info("============================================================");
    }

    public void sendOtpEmail(String toEmail, String otp) {
        log.info("📧 Sending OTP via Brevo API to: {}", toEmail);

        try {
            String subject = "Your OTP for JobAlert";
            String htmlContent = String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>JobAlert OTP</title>
                    <style>
                        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; line-height: 1.6; color: #0f172a; background-color: #f8fafc; margin: 0; padding: 0; }
                        .container { max-width: 480px; margin: 0 auto; padding: 20px; }
                        .card { background-color: #ffffff; border-radius: 16px; padding: 40px 32px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); text-align: center; }
                        .logo { font-size: 24px; font-weight: 700; color: #0f172a; margin-bottom: 4px; }
                        .logo-icon { color: #2563eb; }
                        .badge { background-color: #eff6ff; color: #2563eb; padding: 4px 14px; border-radius: 20px; font-size: 12px; font-weight: 500; display: inline-block; margin: 8px 0 20px 0; }
                        .otp-box { background-color: #f1f5f9; padding: 20px; font-size: 36px; font-weight: 700; letter-spacing: 12px; border-radius: 12px; color: #0f172a; margin: 20px 0; font-family: 'Courier New', monospace; }
                        .otp-label { color: #475569; font-size: 14px; }
                        .validity { color: #94a3b8; font-size: 13px; margin: 16px 0; }
                        .tap-box { background: #f8fafc; padding: 12px; border-radius: 8px; margin-top: 16px; }
                        .tap-box p { margin: 0; font-size: 13px; color: #475569; }
                        .tap-box strong { color: #2563eb; }
                        .footer { margin-top: 24px; padding-top: 20px; border-top: 1px solid #f1f5f9; font-size: 12px; color: #94a3b8; }
                        .footer p { margin: 0; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="card">
                            <div class="logo">
                                <span class="logo-icon">💼</span> JobAlert
                            </div>
                            <div class="badge">🎓 Built for Tap Academy Students</div>
                            
                            <h2 style="margin: 0 0 8px 0; font-size: 22px; color: #0f172a;">Verify Your Email</h2>
                            <p style="color: #475569; font-size: 15px; margin: 0;">Enter the code below to complete your subscription</p>
                            
                            <div class="otp-label">Your OTP Code</div>
                            <div class="otp-box">%s</div>
                            
                            <p class="validity">⏳ This OTP is valid for <strong>5 minutes</strong></p>
                            
                            <div class="tap-box">
                                <p>🎓 <strong>Tap Academy Students</strong> — Get notified about internships and job opportunities</p>
                            </div>
                            
                            <div class="footer">
                                <p>If you didn't request this, please ignore this email.</p>
                                <p style="margin-top: 8px;">© 2026 JobAlert. Made with ❤️ for Tap Academy</p>
                            </div>
                        </div>
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
        log.info("📧 Sending welcome email via Brevo API to: {}", toEmail);

        String subject = "Welcome to JobAlert! 🎉";
        String htmlContent = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Welcome to JobAlert</title>
                <style>
                    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; line-height: 1.6; color: #0f172a; background-color: #f8fafc; margin: 0; padding: 0; }
                    .container { max-width: 480px; margin: 0 auto; padding: 20px; }
                    .card { background-color: #ffffff; border-radius: 16px; padding: 40px 32px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); text-align: center; }
                    .logo { font-size: 24px; font-weight: 700; color: #0f172a; margin-bottom: 4px; }
                    .logo-icon { color: #2563eb; }
                    .badge { background-color: #eff6ff; color: #2563eb; padding: 4px 14px; border-radius: 20px; font-size: 12px; font-weight: 500; display: inline-block; margin: 8px 0 20px 0; }
                    .checkmark { font-size: 64px; margin: 16px 0; }
                    .btn { display: inline-block; padding: 12px 32px; background-color: #2563eb; color: #ffffff; text-decoration: none; border-radius: 8px; font-weight: 500; margin-top: 16px; }
                    .btn:hover { background-color: #1d4ed8; }
                    .success-box { margin: 20px 0; padding: 16px; background: #eff6ff; border-radius: 8px; }
                    .success-box p { margin: 0; font-size: 14px; color: #2563eb; }
                    .footer { margin-top: 24px; padding-top: 20px; border-top: 1px solid #f1f5f9; font-size: 12px; color: #94a3b8; }
                    .footer p { margin: 0; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="card">
                        <div class="logo">
                            <span class="logo-icon">💼</span> JobAlert
                        </div>
                        <div class="badge">🎓 Built for Tap Academy Students</div>
                        
                        <div class="checkmark">✅</div>
                        <h2 style="margin: 0 0 8px 0; font-size: 22px; color: #0f172a;">You're All Set! 🎉</h2>
                        <p style="color: #475569; font-size: 15px; margin: 0;">You'll receive job alerts at <strong>%s</strong></p>
                        
                        <div class="success-box">
                            <p>🎯 You'll be notified when new internships open!</p>
                        </div>
                        
                        <a href="https://your-app.com/jobs" class="btn">Browse Jobs →</a>
                        
                        <div class="footer">
                            <p>© 2026 JobAlert. Made with ❤️ for Tap Academy</p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """, toEmail);

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