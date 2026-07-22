package com.hireflow.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class InterviewRequest {

    @NotNull(message = "Application id is required")
    private Long applicationId;

    @NotNull(message = "Interview date is required")
    private LocalDate interviewDate;

    @NotNull(message = "Interview time is required")
    private LocalTime interviewTime;

    @Size(max = 255, message = "Meeting link cannot exceed 255 characters")
    private String meetingLink;

    @Size(max = 150, message = "Interviewer cannot exceed 150 characters")
    private String interviewer;

    @Size(max = 150, message = "Round cannot exceed 150 characters")
    private String round;
}