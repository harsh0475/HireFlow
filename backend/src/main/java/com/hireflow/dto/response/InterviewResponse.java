package com.hireflow.dto.response;

import com.hireflow.entity.enums.InterviewStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewResponse {

    private Long id;
    private Long applicationId;
    private JobSummaryResponse job;
    private CandidateSummaryResponse candidate;
    private LocalDate interviewDate;
    private LocalTime interviewTime;
    private String meetingLink;
    private String interviewer;
    private String round;
    private InterviewStatus status;
    private String feedback;
    private String outcome;
}