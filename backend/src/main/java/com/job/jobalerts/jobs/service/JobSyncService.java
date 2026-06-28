package com.job.jobalerts.jobs.service;

import com.job.jobalerts.email.service.EmailNotificationService;
import com.job.jobalerts.jobs.entity.Job;
import com.job.jobalerts.jobs.mapper.JobMapper;
import com.job.jobalerts.jobs.repository.JobRepository;
import com.job.jobalerts.tapacademy.dto.TapJobDTO;
import com.job.jobalerts.tapacademy.dto.TapJobsResponse;
import com.job.jobalerts.tapacademy.service.TapAcademyJobFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobSyncService {

    private final TapAcademyJobFetcher jobFetcher;
    private final JobMapper jobMapper;
    private final JobRepository jobRepository;
    private final EmailNotificationService emailNotificationService;

    // Default sync with email notifications enabled
    @Transactional
    public SyncResult syncJobs() {
        return syncJobs(true);
    }

    // Sync with option to control email sending
    @Transactional
    public SyncResult syncJobs(boolean sendEmails) {
        log.info("Starting job sync from Tap Academy (Email sending: {})", sendEmails);

        SyncResult result = new SyncResult();
        List<Job> newJobs = new ArrayList<>();

        try {
            TapJobsResponse response = jobFetcher.fetchJobs();

            if (response == null || response.getData() == null || response.getData().isEmpty()) {
                log.warn("No jobs received from Tap Academy");
                result.setSuccess(true);
                result.setMessage("No jobs to sync");
                return result;
            }

            log.info("Fetched {} jobs from Tap Academy", response.getData().size());

            for (TapJobDTO dto : response.getData()) {
                if (!jobRepository.existsByPortalJobId(dto.getPortalJobId())) {
                    Job job = jobMapper.toEntity(dto);
                    jobRepository.save(job);
                    newJobs.add(job);
                    result.incrementInserted();
                    log.debug("Inserted new job: {}", dto.getJobTitle());
                } else {
                    result.incrementSkipped();
                    log.debug("Skipped existing job: {}", dto.getJobTitle());
                }
            }

            // Set new jobs in result
            result.setNewJobs(newJobs);
            result.setSuccess(true);
            result.setMessage("Sync completed successfully");

            log.info("Sync completed - Inserted: {}, Skipped: {}, New Jobs: {}",
                    result.getInserted(),
                    result.getSkipped(),
                    newJobs.size());

            // Send email notifications for new jobs based on sendEmails flag
            if (!newJobs.isEmpty()) {
                if (sendEmails) {
                    log.info("Sending job alerts for {} new jobs to all subscribers", newJobs.size());
                    emailNotificationService.sendNewJobAlertsToSubscribers(newJobs);
                    result.setEmailsSent(true);
                } else {
                    log.info("Email sending is disabled. Found {} new jobs but notifications were not sent.", newJobs.size());
                    result.setEmailsSent(false);
                    result.setEmailsSkipped(newJobs.size());
                }
            } else {
                log.info("No new jobs found, skipping email notifications");
                result.setEmailsSent(false);
            }

        } catch (Exception e) {
            log.error("Job sync failed", e);
            result.setSuccess(false);
            result.setMessage("Sync failed: " + e.getMessage());
            result.setNewJobs(new ArrayList<>());
        }

        return result;
    }

    public static class SyncResult {
        private boolean success;
        private String message;
        private int inserted;
        private int skipped;
        private boolean emailsSent;
        private int emailsSkipped;
        private List<Job> newJobs = new ArrayList<>();

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public int getInserted() { return inserted; }
        public void setInserted(int inserted) { this.inserted = inserted; }
        public void incrementInserted() { this.inserted++; }

        public int getSkipped() { return skipped; }
        public void setSkipped(int skipped) { this.skipped = skipped; }
        public void incrementSkipped() { this.skipped++; }

        public List<Job> getNewJobs() { return newJobs; }
        public void setNewJobs(List<Job> newJobs) { this.newJobs = newJobs; }

        public boolean isEmailsSent() { return emailsSent; }
        public void setEmailsSent(boolean emailsSent) { this.emailsSent = emailsSent; }

        public int getEmailsSkipped() { return emailsSkipped; }
        public void setEmailsSkipped(int emailsSkipped) { this.emailsSkipped = emailsSkipped; }

        public int getNewJobsCount() { return newJobs != null ? newJobs.size() : 0; }
    }
}