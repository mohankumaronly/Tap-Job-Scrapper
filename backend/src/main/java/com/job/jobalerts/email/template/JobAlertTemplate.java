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
                <div style="border: 1px solid #ddd; border-radius: 8px; padding: 15px; margin-bottom: 15px; background-color: #f9f9f9;">
                    <h3 style="color: #4CAF50; margin: 0 0 10px 0;">%s</h3>
                    <p style="margin: 5px 0;"><strong>Role:</strong> %s</p>
                    <p style="margin: 5px 0;"><strong>Location:</strong> %s</p>
                    <p style="margin: 5px 0;"><strong>Package:</strong> %.1f LPA</p>
                    <p style="margin: 5px 0;"><strong>Interview Date:</strong> %s</p>
                    <a href="%s" style="display: inline-block; margin-top: 10px; padding: 8px 15px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px;">View Details →</a>
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
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; }
                    .header { text-align: center; padding-bottom: 20px; border-bottom: 1px solid #eee; }
                    .logo { color: #4CAF50; font-size: 24px; font-weight: bold; }
                    .count { background-color: #4CAF50; color: white; padding: 5px 10px; border-radius: 20px; display: inline-block; }
                    .footer { margin-top: 20px; padding-top: 20px; border-top: 1px solid #eee; font-size: 12px; color: #888; text-align: center; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <div class="logo">🎯 Job Alerts</div>
                    </div>
                    
                    <h2>New Jobs Available! 🚀</h2>
                    <p>We found <span class="count">%d new job%s</span> that might interest you:</p>
                    
                    %s
                    
                    <div class="footer">
                        <p>You received this email because you subscribed to Job Alerts.</p>
                        <p>To unsubscribe, <a href="#">click here</a></p>
                        <p>&copy; 2026 Job Alerts. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """,
                newJobs.size(),
                newJobs.size() > 1 ? "s" : "",
                jobsHtml.toString()
        );
    }
}