package com.job.jobalerts.email.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${MAIL_FROM:mohankumaronly81@gmail.com}")
    private String fromEmail;

    @Value("${MAIL_FROM_NAME:Job Alerts}")
    private String fromName;

    @PostConstruct
    public void init() {
        log.info("========== EMAIL SERVICE DEBUG ==========");
        log.info("From Email: {}", fromEmail);
        log.info("From Name: {}", fromName);
        log.info("=========================================");
    }

    public void sendOtpEmail(String toEmail, String otp) {
        log.info("Sending OTP to: {}", toEmail);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(toEmail);
            helper.setSubject("Your OTP for Job Alerts");

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

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("✅ OTP email sent successfully to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send OTP email to: {}", toEmail, e);
            System.out.println("=========================================");
            System.out.println("📧 EMAIL FAILED - Using Console OTP");
            System.out.println("OTP for " + toEmail + ": " + otp);
            System.out.println("=========================================");
        }
    }

    public void sendWelcomeEmail(String toEmail) {
        log.info("Sending welcome email to: {}", toEmail);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(toEmail);
            helper.setSubject("Welcome to Job Alerts! 🎉");

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

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("✅ Welcome email sent successfully to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send welcome email to: {}", toEmail, e);
        }
    }

    public void sendEmail(String toEmail, String subject, String htmlContent) {
        log.info("Sending email to: {}", toEmail);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("✅ Email sent successfully to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send email to: {}", toEmail, e);
        }
    }
}