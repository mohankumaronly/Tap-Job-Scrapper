package com.job.jobalerts.email.template;

import com.job.jobalerts.email.dto.JobAlertDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobAlertTemplate {

    public String buildNewJobAlertHtml(List<JobAlertDTO> newJobs) {
        StringBuilder jobsHtml = new StringBuilder();

        for (JobAlertDTO job : newJobs) {
            jobsHtml.append(String.format("""
                <div style="border: 1px solid #e2e8f0; border-radius: 12px; padding: 20px; margin-bottom: 20px; background-color: #ffffff; box-shadow: 0 1px 3px rgba(0,0,0,0.05);">
                    <h3 style="color: #0f172a; margin: 0 0 8px 0; font-size: 18px; font-weight: 600;">%s</h3>
                    <p style="margin: 4px 0; color: #2563eb; font-weight: 500; font-size: 14px;">%s</p>
                    <div style="margin: 12px 0; padding: 12px 0; border-top: 1px solid #f1f5f9; border-bottom: 1px solid #f1f5f9;">
                        <p style="margin: 4px 0; color: #475569; font-size: 14px;"><strong>📍 Location:</strong> %s</p>
                        <p style="margin: 4px 0; color: #475569; font-size: 14px;"><strong>💰 Package:</strong> ₹%.1f LPA</p>
                        <p style="margin: 4px 0; color: #475569; font-size: 14px;"><strong>📅 Interview Date:</strong> %s</p>
                    </div>
                    <a href="%s" style="display: inline-block; padding: 10px 24px; background-color: #2563eb; color: #ffffff; text-decoration: none; border-radius: 8px; font-size: 14px; font-weight: 500;">View Details →</a>
                </div>
                """,
                    job.getJobTitle(),
                    job.getJobRole(),
                    job.getLocation(),
                    job.getPackageLpa(),
                    job.getInterviewDate() != null ? job.getInterviewDate() : "To be announced",
                    job.getJobUrl()
            ));
        }

        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>New Jobs Available!</title>
                <style>
                    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; line-height: 1.6; color: #0f172a; background-color: #f8fafc; margin: 0; padding: 0; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .card { background-color: #ffffff; border-radius: 16px; padding: 40px 30px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); }
                    .header { text-align: center; padding-bottom: 24px; border-bottom: 2px solid #f1f5f9; margin-bottom: 24px; }
                    .logo { display: flex; align-items: center; justify-content: center; gap: 10px; color: #0f172a; font-size: 24px; font-weight: 700; }
                    .logo-icon { color: #2563eb; font-size: 28px; }
                    .badge { background-color: #eff6ff; color: #2563eb; padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 500; display: inline-block; margin-top: 8px; }
                    .count-badge { display: inline-block; background-color: #2563eb; color: #ffffff; padding: 2px 12px; border-radius: 20px; font-weight: 600; font-size: 14px; }
                    .footer { margin-top: 24px; padding-top: 24px; border-top: 2px solid #f1f5f9; font-size: 12px; color: #94a3b8; text-align: center; }
                    .footer a { color: #2563eb; text-decoration: none; }
                    .job-list { margin-top: 20px; }
                    .tap-badge { background: #f1f5f9; padding: 8px 16px; border-radius: 8px; text-align: center; margin: 16px 0; font-size: 13px; color: #475569; }
                    .tap-badge span { color: #2563eb; font-weight: 600; }
                    .btn-secondary { display: inline-block; padding: 8px 20px; background-color: #f1f5f9; color: #0f172a; text-decoration: none; border-radius: 8px; font-size: 13px; margin-top: 12px; }
                    @media only screen and (max-width: 480px) {
                        .container { padding: 10px; }
                        .card { padding: 20px 16px; }
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="card">
                        <div class="header">
                            <div class="logo">
                                <span class="logo-icon">💼</span>
                                <span>JobAlert</span>
                            </div>
                            <div class="badge">Built for Tap Academy Students</div>
                        </div>
                        
                        <h2 style="margin: 0 0 8px 0; font-size: 24px; color: #0f172a;">New Jobs Available! 🚀</h2>
                        <p style="margin: 0 0 4px 0; color: #475569; font-size: 16px;">
                            We found <span class="count-badge">%d new job%s</span> that might interest you
                        </p>
                        <div class="tap-badge">
                            🎓 <span>Tap Academy</span> — Tailored opportunities for students and interns
                        </div>
                        
                        <div class="job-list">
                            %s
                        </div>
                        
                        <div style="text-align: center; margin-top: 24px;">
                            <a href="%s" style="display: inline-block; padding: 12px 32px; background-color: #2563eb; color: #ffffff; text-decoration: none; border-radius: 8px; font-weight: 500;">View All Jobs</a>
                        </div>
                        
                        <div class="footer">
                            <p>You received this email because you subscribed to JobAlert.</p>
                            <p>To unsubscribe, <a href="#">click here</a></p>
                            <p style="margin-top: 8px; color: #cbd5e1;">© 2026 JobAlert. All rights reserved. Made with ❤️ for Tap Academy students</p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """,
                newJobs.size(),
                newJobs.size() > 1 ? "s" : "",
                jobsHtml.toString(),
                "https://your-app.com/jobs" // Replace with your actual URL
        );
    }
}