package com.hireflow.dto.response;

import com.hireflow.entity.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ApplicationResponse {

    private Long id;

    private JobSummaryResponse job;

    private CandidateSummaryResponse candidate;

    private String coverLetter;

    private String resumeUrl;

    private ApplicationStatus status;

    private String recruiterNotes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}