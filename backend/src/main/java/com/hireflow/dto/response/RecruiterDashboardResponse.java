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
public class RecruiterDashboardResponse {

    private long jobsPosted;

    private long activeJobs;

    private long totalApplications;

    private long interviewsScheduled;

    private long hired;
}