package com.job.jobalerts.jobs.mapper;

import com.job.jobalerts.jobs.dto.JobResponseDTO;
import com.job.jobalerts.jobs.entity.Job;
import com.job.jobalerts.tapacademy.dto.TapJobDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    // Convert TapJobDTO to Job Entity
    public Job toEntity(TapJobDTO dto) {
        if (dto == null) {
            return null;
        }

        Job job = new Job();

        // Core fields
        job.setPortalJobId(dto.getPortalJobId());
        job.setJobId(dto.getJobId());
        job.setJobTitle(dto.getJobTitle());
        job.setJobRole(dto.getJobRole());
        job.setPackageLpa(dto.getPackageLpa());

        // Location (first location from list)
        String firstLocation = dto.getFirstLocation();
        job.setLocation(firstLocation);

        // Status fields
        job.setExpired(dto.getExpired() != null ? dto.getExpired() : false);
        job.setApplied(false); // Default to false

        // Date fields
        if (dto.getInterviewDate() != null) {
            try {
                job.setInterviewDate(LocalDateTime.parse(dto.getInterviewDate(), DATE_FORMATTER));
            } catch (Exception e) {
                // Log warning and continue
                System.out.println("Failed to parse interview date: " + dto.getInterviewDate());
            }
        }

        if (dto.getExpiresIn() != null) {
            try {
                job.setExpiresIn(LocalDateTime.parse(dto.getExpiresIn(), DATE_FORMATTER));
            } catch (Exception e) {
                System.out.println("Failed to parse expiry date: " + dto.getExpiresIn());
            }
        }

        return job;
    }

    // Convert Job Entity to JobResponseDTO
    public JobResponseDTO toResponseDTO(Job job) {
        if (job == null) {
            return null;
        }

        return JobResponseDTO.builder()
                .id(job.getId())
                .jobId(job.getJobId())
                .jobTitle(job.getJobTitle())
                .jobRole(job.getJobRole())
                .packageLpa(job.getPackageLpa())
                .location(job.getLocation())
                .expired(job.getExpired())
                .applied(job.getApplied())
                .interviewDate(job.getInterviewDate())
                .expiresIn(job.getExpiresIn())
                .createdAt(job.getCreatedAt())
                .build();
    }

    // Convert List of Job Entities to List of JobResponseDTOs
    public List<JobResponseDTO> toResponseDTOList(List<Job> jobs) {
        if (jobs == null) {
            return List.of();
        }

        return jobs.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}