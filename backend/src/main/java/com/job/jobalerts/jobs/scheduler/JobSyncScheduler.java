package com.job.jobalerts.jobs.scheduler;

import com.job.jobalerts.jobs.service.JobSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobSyncScheduler {

    private final JobSyncService jobSyncService;

    @Value("${SCRAPER_ENABLED:true}")
    private boolean scraperEnabled;

    @Value("${SCRAPER_CRON_EXPRESSION:0 0 */6 * * *}")
    private String cronExpression;

    @Value("${SCRAPER_LOGGING_ENABLED:true}")
    private boolean loggingEnabled;

    @Value("${SCRAPER_SEND_EMAILS:true}")
    private boolean sendEmails;

    @Scheduled(cron = "${SCRAPER_CRON_EXPRESSION:0 0 */6 * * *}")
    public void scheduledJobSync() {
        if (!scraperEnabled) {
            if (loggingEnabled) {
                log.info("===== SCRAPER IS DISABLED - Skipping scheduled sync =====");
            }
            return;
        }

        if (loggingEnabled) {
            log.info("===== SCHEDULED JOB SYNC STARTED (Cron: {}) =====", cronExpression);
        }

        long startTime = System.currentTimeMillis();

        try {
            JobSyncService.SyncResult result = jobSyncService.syncJobs(sendEmails);

            long duration = System.currentTimeMillis() - startTime;

            if (loggingEnabled) {
                log.info("Scheduled sync completed in {} ms", duration);
                log.info("Inserted: {}, Skipped: {}, New Jobs: {}",
                        result.getInserted(),
                        result.getSkipped(),
                        result.getNewJobs().size());

                if (result.isSuccess()) {
                    log.info("✅ Sync successful: {}", result.getMessage());
                } else {
                    log.error("❌ Sync failed: {}", result.getMessage());
                }
            }

        } catch (Exception e) {
            log.error("Scheduled sync failed", e);
        }

        if (loggingEnabled) {
            log.info("===== SCHEDULED JOB SYNC ENDED =====");
        }
    }

    public void manualJobSync() {
        if (!scraperEnabled) {
            log.warn("Manual sync requested but scraper is disabled");
            return;
        }

        log.info("===== MANUAL JOB SYNC STARTED =====");
        long startTime = System.currentTimeMillis();

        try {
            JobSyncService.SyncResult result = jobSyncService.syncJobs(sendEmails);
            long duration = System.currentTimeMillis() - startTime;
            log.info("Manual sync completed in {} ms", duration);
            log.info("Inserted: {}, Skipped: {}, New Jobs: {}",
                    result.getInserted(),
                    result.getSkipped(),
                    result.getNewJobs().size());

        } catch (Exception e) {
            log.error("Manual sync failed", e);
        }

        log.info("===== MANUAL JOB SYNC ENDED =====");
    }

    public ScraperStatus getScraperStatus() {
        return new ScraperStatus(scraperEnabled, cronExpression, sendEmails, loggingEnabled);
    }

    public record ScraperStatus(boolean enabled, String cronExpression, boolean sendEmails, boolean loggingEnabled) {}
}