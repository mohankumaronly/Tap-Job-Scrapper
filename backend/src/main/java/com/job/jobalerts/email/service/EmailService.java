package com.job.jobalerts.email.service;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${BREVO_USERNAME}")
    private String fromEmail;

    @Value("${mail.from.name:Job Alerts}")
    private String fromName;

    @Value("${BREVO_USERNAME}")
    private String username;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostConstruct
    public void init() {
        log.info("========== BREVO SMTP EMAIL SERVICE INITIALIZED ==========");
        log.info("Username: {}", username);
        log.info("From Email: {}", fromEmail);
        log.info("From Name: {}", fromName);
        log.info("==========================================================");
    }

    public void sendOtpEmail(String toEmail, String otp) {
        log.info("Sending OTP to: {}", toEmail);

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
    }

    public void sendWelcomeEmail(String toEmail) {
        log.info("Sending welcome email to: {}", toEmail);

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
            // Validate email
            if (fromEmail == null || fromEmail.isEmpty()) {
                log.error("From email is null or empty. Check BREVO_USERNAME in .env file");
                printFallback(toEmail, subject, htmlContent, "From email not configured");
                return;
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("✅ Email sent successfully to: {}", toEmail);

        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", toEmail, e);
            printFallback(toEmail, subject, htmlContent, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error sending email to: {}", toEmail, e);
            printFallback(toEmail, subject, htmlContent, e.getMessage());
        }
    }

    private void printFallback(String toEmail, String subject, String htmlContent, String error) {
        System.out.println("=========================================");
        System.out.println("📧 EMAIL FAILED - Using Console Output");
        System.out.println("To: " + toEmail);
        System.out.println("Subject: " + subject);
        System.out.println("Error: " + error);
        System.out.println("Content Preview: " + htmlContent.substring(0, Math.min(100, htmlContent.length())) + "...");
        System.out.println("=========================================");
    }
}