package com.hireflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformStatisticsResponse {

    private long totalRecruiters;

    private long totalCandidates;

    private List<CompanyJobCountResponse> jobsPerCompany;

    private List<JobApplicationCountResponse> applicationsPerJob;

    private List<MonthlyRegistrationResponse> monthlyRegistrations;

    private long totalUsers;

    private long totalCompanies;

    private long totalJobs;

    private long activeJobs;

    private long totalApplications;

    private long totalInterviews;
}