package com.job.jobalerts.tapacademy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TapJobDTO {

    @JsonProperty("_id")
    private String portalJobId;

    @JsonProperty("jobId")
    private Integer jobId;

    @JsonProperty("jobTitle")
    private String jobTitle;

    @JsonProperty("jobRole")
    private String jobRole;

    @JsonProperty("package")
    private Double packageLpa;

    @JsonProperty("jobLocation")
    private List<String> jobLocation;

    @JsonProperty("expired")
    private Boolean expired;

    @JsonProperty("isApplied")
    private Boolean isApplied;

    @JsonProperty("companyId")
    private String companyId;

    @JsonProperty("createdAt")
    private String createdAt;

    @JsonProperty("updatedAt")
    private String updatedAt;

    @JsonProperty("expiresIn")
    private String expiresIn;

    @JsonProperty("interviewDate")
    private String interviewDate;

    // Helper method to get first location
    public String getFirstLocation() {
        if (jobLocation != null && !jobLocation.isEmpty()) {
            return jobLocation.get(0);
        }
        return "Unknown";
    }
}