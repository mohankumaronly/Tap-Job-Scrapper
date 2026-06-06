package com.job.jobalerts.jobs.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class JobRequestDTO {
    private String portalJobId;
    private Integer jobId;
    private String jobTitle;
    private String jobRole;
    private Double packageLpa;
    private String location;
    private Boolean expired;
    private Boolean applied;
    private LocalDateTime interviewDate;
    private LocalDateTime expiresIn;
}