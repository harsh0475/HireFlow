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
public class CandidateDashboardResponse {

    private long appliedJobs;

    private long shortlisted;

    private long interviewsScheduled;

    private long rejected;

    private long hired;

    private long withdrawn;
}