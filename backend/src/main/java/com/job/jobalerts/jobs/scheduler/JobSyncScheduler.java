package com.job.jobalerts.jobs.scheduler;

import com.job.jobalerts.jobs.service.JobSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobSyncScheduler {

    private final JobSyncService jobSyncService;


    @Scheduled(cron = "0 0 */6 * * *")
    public void scheduledJobSync() {
        log.info("===== SCHEDULED JOB SYNC STARTED (Every 6 hours) =====");
        long startTime = System.currentTimeMillis();

        try {
            JobSyncService.SyncResult result = jobSyncService.syncJobs();

            long duration = System.currentTimeMillis() - startTime;
            log.info("Scheduled sync completed in {} ms", duration);
            log.info("Inserted: {}, Skipped: {}, New Jobs: {}, Emails Sent: {}",
                    result.getInserted(),
                    result.getSkipped(),
                    result.getNewJobs().size(),
                    result.getInserted() > 0);

        } catch (Exception e) {
            log.error("Scheduled sync failed", e);
        }

        log.info("===== SCHEDULED JOB SYNC ENDED =====");
    }
}