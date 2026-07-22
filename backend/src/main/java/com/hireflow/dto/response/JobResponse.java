package com.hireflow.dto.response;

import com.hireflow.entity.enums.EmploymentType;
import com.hireflow.entity.enums.ExperienceLevel;
import com.hireflow.entity.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String responsibilities;
    private String requirements;
    private Set<String> skills;
    private String location;
    private Boolean remote;
    private Double minSalary;
    private Double maxSalary;
    private String currency;
    private EmploymentType employmentType;
    private ExperienceLevel experienceLevel;
    private JobStatus status;
    private Integer openings;
    private LocalDate applicationDeadline;
    private Boolean active;
    private CompanyResponse company;
    private RecruiterSummaryResponse createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}