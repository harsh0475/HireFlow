package com.hireflow.dto.response;

import com.hireflow.entity.enums.EmploymentType;
import com.hireflow.entity.enums.ExperienceLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobSummaryResponse {

    private Long id;

    private String title;

    private String location;

    private EmploymentType employmentType;

    private ExperienceLevel experienceLevel;

    private CompanyResponse company;
}