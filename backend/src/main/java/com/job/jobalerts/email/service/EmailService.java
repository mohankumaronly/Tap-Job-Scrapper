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

    @Value("${mail.from.name:JobAlert}")
    private String fromName;

    @Value("${BREVO_USERNAME}")
    private String username;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostConstruct
    public void init() {
        log.info("========== JOBALERT SMTP EMAIL SERVICE INITIALIZED ==========");
        log.info("Username: {}", username);
        log.info("From Email: {}", fromEmail);
        log.info("From Name: {}", fromName);
        log.info("==============================================================");
    }

    public void sendOtpEmail(String toEmail, String otp) {
        log.info("📧 Sending OTP via SMTP to: {}", toEmail);

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
    }

    public void sendWelcomeEmail(String toEmail) {
        log.info("📧 Sending welcome email via SMTP to: {}", toEmail);

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