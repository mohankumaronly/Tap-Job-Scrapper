package com.job.jobalerts.email.service;

import com.job.jobalerts.auth.entity.User;
import com.job.jobalerts.auth.repository.UserRepository;
import com.job.jobalerts.email.dto.JobAlertDTO;
import com.job.jobalerts.email.template.JobAlertTemplate;
import com.job.jobalerts.jobs.entity.Job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final JobAlertTemplate jobAlertTemplate;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

    /**
     * Send job alerts to all subscribed users for new jobs
     */
    public void sendNewJobAlertsToSubscribers(List<Job> newJobs) {
        if (newJobs == null || newJobs.isEmpty()) {
            log.info("No new jobs to send alerts for");
            return;
        }

        List<User> subscribers = userRepository.findBySubscribedTrue();

        if (subscribers.isEmpty()) {
            log.info("No active subscribers found");
            return;
        }

        log.info("Found {} new jobs. Sending alerts to {} subscribers", newJobs.size(), subscribers.size());

        // Convert Jobs to DTOs
        List<JobAlertDTO> jobAlertDTOS = newJobs.stream()
                .map(this::convertToJobAlertDTO)
                .collect(Collectors.toList());

        // Send ONE email per user with ALL new jobs
        for (User user : subscribers) {
            try {
                sendJobAlertEmail(user, jobAlertDTOS);
                // Small delay to avoid rate limiting
                Thread.sleep(100);
            } catch (Exception e) {
                log.error("Failed to send job alert to {}: {}", user.getEmail(), e.getMessage());
            }
        }

        log.info("Successfully sent job alerts to {} subscribers", subscribers.size());
    }

    /**
     * Send single job alert email to a specific user (for testing)
     */
    public void sendTestJobAlert(String testEmail, List<Job> newJobs) {
        log.info("Sending test job alert to: {}", testEmail);

        List<JobAlertDTO> jobAlertDTOS = newJobs.stream()
                .map(this::convertToJobAlertDTO)
                .collect(Collectors.toList());

        String subject = "🔔 New Job Alert: " + newJobs.size() + " new position" + (newJobs.size() > 1 ? "s" : "");
        String htmlContent = jobAlertTemplate.buildNewJobAlertHtml(jobAlertDTOS);

        emailService.sendEmail(testEmail, subject, htmlContent);
    }

    private void sendJobAlertEmail(User user, List<JobAlertDTO> jobs) {
        String subject = "🔔 New Job Alert: " + jobs.size() + " new position" + (jobs.size() > 1 ? "s" : "");
        String htmlContent = jobAlertTemplate.buildNewJobAlertHtml(jobs);

        emailService.sendEmail(user.getEmail(), subject, htmlContent);
        log.debug("Job alert sent to: {}", user.getEmail());
    }

    private JobAlertDTO convertToJobAlertDTO(Job job) {
        return JobAlertDTO.builder()
                .jobTitle(job.getJobTitle())
                .jobRole(job.getJobRole())
                .location(job.getLocation())
                .packageLpa(job.getPackageLpa())
                .interviewDate(job.getInterviewDate() != null ?
                        job.getInterviewDate().format(dateFormatter) : null)
                .jobUrl(baseUrl + "/api/jobs/portal/" + job.getPortalJobId())
                .build();
    }
}