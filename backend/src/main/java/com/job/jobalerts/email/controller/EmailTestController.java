package com.job.jobalerts.email.controller;

import com.job.jobalerts.email.service.EmailNotificationService;
import com.job.jobalerts.jobs.entity.Job;
import com.job.jobalerts.jobs.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailNotificationService emailNotificationService;
    private final JobService jobService;

    @PostMapping("/test/job-alert")
    public ResponseEntity<Map<String, String>> testJobAlert(@RequestParam String email) {
        List<Job> recentJobs = jobService.getActiveJobs();

        if (recentJobs.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No jobs found to send");
            return ResponseEntity.ok(response);
        }

        // Send test email with first 5 jobs
        List<Job> testJobs = recentJobs.size() > 5 ? recentJobs.subList(0, 5) : recentJobs;
        emailNotificationService.sendTestJobAlert(email, testJobs);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Test job alert sent to " + email);
        response.put("jobsCount", String.valueOf(testJobs.size()));
        return ResponseEntity.ok(response);
    }
}