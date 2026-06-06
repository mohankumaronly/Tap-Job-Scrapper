package com.job.jobalerts.jobs.controller;

import com.job.jobalerts.jobs.dto.JobResponseDTO;
import com.job.jobalerts.jobs.entity.Job;
import com.job.jobalerts.jobs.mapper.JobMapper;
import com.job.jobalerts.jobs.service.JobService;
import com.job.jobalerts.jobs.service.JobSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final JobMapper jobMapper;
    private final JobSyncService jobSyncService;

    // Get all jobs
    @GetMapping
    public ResponseEntity<List<JobResponseDTO>> getAllJobs() {
        log.info("GET /api/jobs - Fetching all jobs");
        List<Job> jobs = jobService.getAllJobs();
        List<JobResponseDTO> response = jobMapper.toResponseDTOList(jobs);
        return ResponseEntity.ok(response);
    }

    // Get active jobs (not expired)
    @GetMapping("/active")
    public ResponseEntity<List<JobResponseDTO>> getActiveJobs() {
        log.info("GET /api/jobs/active - Fetching active jobs");
        List<Job> jobs = jobService.getActiveJobs();
        List<JobResponseDTO> response = jobMapper.toResponseDTOList(jobs);
        return ResponseEntity.ok(response);
    }

    // Get job by portal job ID
    @GetMapping("/portal/{portalJobId}")
    public ResponseEntity<JobResponseDTO> getJobByPortalId(@PathVariable String portalJobId) {
        log.info("GET /api/jobs/portal/{} - Fetching job", portalJobId);

        return jobService.getJobByPortalId(portalJobId)
                .map(jobMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get job by database ID
    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDTO> getJobById(@PathVariable Long id) {
        log.info("GET /api/jobs/{} - Fetching job", id);

        return jobService.getJobById(id)
                .map(jobMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Sync jobs from Tap Academy to database
    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> syncJobs() {
        log.info("POST /api/jobs/sync - Triggering job sync from Tap Academy");

        try {
            JobSyncService.SyncResult result = jobSyncService.syncJobs();

            Map<String, Object> response = new HashMap<>();
            response.put("success", result.isSuccess());
            response.put("message", result.getMessage());
            response.put("inserted", result.getInserted());
            response.put("skipped", result.getSkipped());
            response.put("newJobsCount", result.getNewJobs().size());
            response.put("emailSent", result.getInserted() > 0);

            if (result.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            log.error("Sync failed", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Sync failed: " + e.getMessage());
            response.put("inserted", 0);
            response.put("skipped", 0);
            response.put("newJobsCount", 0);
            response.put("emailSent", false);
            return ResponseEntity.status(500).body(response);
        }
    }
}