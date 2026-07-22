package com.hireflow.dto.request;

import com.hireflow.entity.enums.EmploymentType;
import com.hireflow.entity.enums.ExperienceLevel;
import com.hireflow.entity.enums.JobStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobFilterRequest {

    private String keyword;

    private String location;

    private EmploymentType employmentType;

    private ExperienceLevel experienceLevel;

    private JobStatus status;

    private Long companyId;

    private Double minSalary;

    private Double maxSalary;

    private String skill;

    private Boolean remote;
}