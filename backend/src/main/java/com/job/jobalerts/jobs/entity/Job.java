package com.job.jobalerts.jobs.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "portal_job_id", unique = true, nullable = false)
    private String portalJobId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "job_role")
    private String jobRole;

    @Column(name = "package_lpa")
    private Double packageLpa;

    @Column(name = "location")
    private String location;

    @Column(name = "expired")
    private Boolean expired;

    @Column(name = "applied")
    private Boolean applied;

    @Column(name = "interview_date")
    private LocalDateTime interviewDate;

    @Column(name = "expires_in")
    private LocalDateTime expiresIn;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (expired == null) expired = false;
        if (applied == null) applied = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}