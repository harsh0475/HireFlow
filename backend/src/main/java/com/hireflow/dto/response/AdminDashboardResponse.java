package com.hireflow.dto.response;

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
public class AdminDashboardResponse {

    private long totalUsers;

    private long totalCandidates;

    private long totalRecruiters;

    private long totalCompanies;

    private long totalJobs;

    private long activeJobs;

    private long totalApplications;

    private long totalInterviews;
}