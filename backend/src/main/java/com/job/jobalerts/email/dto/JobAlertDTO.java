package com.job.jobalerts.email.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobAlertDTO {
    private String jobTitle;
    private String jobRole;
    private String location;
    private Double packageLpa;
    private String interviewDate;
    private String jobUrl;
}