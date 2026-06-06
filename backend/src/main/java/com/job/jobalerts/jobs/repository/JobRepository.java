package com.job.jobalerts.jobs.repository;

import com.job.jobalerts.jobs.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    // Find by portal job ID (Tap Academy's Mongo ID)
    Optional<Job> findByPortalJobId(String portalJobId);

    // Check if job exists by portal job ID
    boolean existsByPortalJobId(String portalJobId);

    // Find all non-expired jobs
    List<Job> findByExpiredFalse();

    // Find all active jobs (not expired)
    List<Job> findByExpiredFalseOrderByCreatedAtDesc();

    // Find by job ID (Tap Academy's job number)
    Optional<Job> findByJobId(Integer jobId);
}