package com.job.jobalerts.jobs.service;

import com.job.jobalerts.jobs.entity.Job;
import com.job.jobalerts.jobs.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    // Get all jobs
    @Transactional(readOnly = true)
    public List<Job> getAllJobs() {
        log.info("Fetching all jobs");
        return jobRepository.findAll();
    }

    // Get active jobs (not expired)
    @Transactional(readOnly = true)
    public List<Job> getActiveJobs() {
        log.info("Fetching active jobs");
        return jobRepository.findByExpiredFalseOrderByCreatedAtDesc();
    }

    // Get job by ID
    @Transactional(readOnly = true)
    public Optional<Job> getJobById(Long id) {
        log.info("Fetching job by id: {}", id);
        return jobRepository.findById(id);
    }

    // Get job by portal job ID
    @Transactional(readOnly = true)
    public Optional<Job> getJobByPortalId(String portalJobId) {
        log.info("Fetching job by portal ID: {}", portalJobId);
        return jobRepository.findByPortalJobId(portalJobId);
    }

    // Check if job exists
    @Transactional(readOnly = true)
    public boolean jobExists(String portalJobId) {
        log.info("Checking if job exists: {}", portalJobId);
        return jobRepository.existsByPortalJobId(portalJobId);
    }

    // Save job
    @Transactional
    public Job saveJob(Job job) {
        log.info("Saving job: {}", job.getJobTitle());
        return jobRepository.save(job);
    }

    // Delete job
    @Transactional
    public void deleteJob(Long id) {
        log.info("Deleting job with id: {}", id);
        jobRepository.deleteById(id);
    }
}