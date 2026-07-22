package com.hireflow.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class InterviewUpdateRequest {

    private LocalDate interviewDate;

    private LocalTime interviewTime;

    @Size(max = 255, message = "Meeting link cannot exceed 255 characters")
    private String meetingLink;

    @Size(max = 150, message = "Interviewer cannot exceed 150 characters")
    private String interviewer;

    @Size(max = 150, message = "Round cannot exceed 150 characters")
    private String round;
}