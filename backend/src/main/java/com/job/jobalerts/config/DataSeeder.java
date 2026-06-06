package com.job.jobalerts.config;

import com.job.jobalerts.jobs.entity.Job;
import com.job.jobalerts.jobs.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final JobService jobService;

    @Override
    public void run(String... args) throws Exception {
        // Check if we already have jobs
        if (jobService.getAllJobs().isEmpty()) {
            log.info("Seeding sample job data...");

            // Sample Job 1
            Job job1 = Job.builder()
                    .portalJobId("6a22d270124140207a9f815e")
                    .jobId(2523)
                    .jobTitle("JAVA Full Stack Developer")
                    .jobRole("Java Full Stack Developer")
                    .packageLpa(2.4)
                    .location("Bengaluru")
                    .expired(false)
                    .applied(false)
                    .interviewDate(LocalDateTime.now().plusDays(5))
                    .expiresIn(LocalDateTime.now().plusDays(30))
                    .build();

            // Sample Job 2
            Job job2 = Job.builder()
                    .portalJobId("6a22d270124140207a9f815f")
                    .jobId(2522)
                    .jobTitle("Junior Python Developer")
                    .jobRole("Python Developer")
                    .packageLpa(3.0)
                    .location("Chennai")
                    .expired(false)
                    .applied(false)
                    .interviewDate(LocalDateTime.now().plusDays(3))
                    .expiresIn(LocalDateTime.now().plusDays(25))
                    .build();

            // Sample Job 3
            Job job3 = Job.builder()
                    .portalJobId("6a22d270124140207a9f8160")
                    .jobId(2521)
                    .jobTitle("React Frontend Developer")
                    .jobRole("Frontend Developer")
                    .packageLpa(3.5)
                    .location("Coimbatore")
                    .expired(false)
                    .applied(false)
                    .interviewDate(LocalDateTime.now().plusDays(7))
                    .expiresIn(LocalDateTime.now().plusDays(20))
                    .build();

            jobService.saveJob(job1);
            jobService.saveJob(job2);
            jobService.saveJob(job3);

            log.info("3 sample jobs seeded successfully!");
        } else {
            log.info("Jobs already exist in database. Total jobs: {}", jobService.getAllJobs().size());
        }
    }
}