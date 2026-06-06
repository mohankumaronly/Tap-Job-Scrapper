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

    @Transactional
    public SyncResult syncJobs() {
        log.info("Starting job sync from Tap Academy");

        SyncResult result = new SyncResult();
        List<Job> newJobs = new ArrayList<>();

        try {
            TapJobsResponse response = jobFetcher.fetchJobs();

            if (response == null || response.getData() == null || response.getData().isEmpty()) {
                log.warn("No jobs received from Tap Academy");
                result.setMessage("No jobs to sync");
                result.setSuccess(true);
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

            // Send email notifications for new jobs
            if (!newJobs.isEmpty()) {
                log.info("Sending job alerts for {} new jobs to all subscribers", newJobs.size());
                emailNotificationService.sendNewJobAlertsToSubscribers(newJobs);
            } else {
                log.info("No new jobs found, skipping email notifications");
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
    }
}