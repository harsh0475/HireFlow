package com.hireflow.dto.request;

import com.hireflow.entity.enums.EmploymentType;
import com.hireflow.entity.enums.ExperienceLevel;
import com.hireflow.entity.enums.JobStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class JobRequest {

    @NotBlank(message = "Job title is required")
    @Size(max = 200, message = "Job title cannot exceed 200 characters")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    private String responsibilities;

    private String requirements;

    private Set<String> skills;

    @Size(max = 150, message = "Location cannot exceed 150 characters")
    private String location;

    private Boolean remote;

    @Positive(message = "Minimum salary must be positive")
    private Double minSalary;

    @Positive(message = "Maximum salary must be positive")
    private Double maxSalary;

    @Size(max = 10, message = "Currency code cannot exceed 10 characters")
    private String currency;

    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;

    @NotNull(message = "Experience level is required")
    private ExperienceLevel experienceLevel;

    private JobStatus status;

    @Positive(message = "Openings must be at least 1")
    private Integer openings;

    private LocalDate applicationDeadline;

    @NotNull(message = "Company is required")
    private Long companyId;
}