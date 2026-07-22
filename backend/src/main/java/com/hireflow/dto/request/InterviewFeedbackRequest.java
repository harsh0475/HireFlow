package com.hireflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterviewFeedbackRequest {

    @NotBlank(message = "Feedback is required")
    @Size(max = 3000, message = "Feedback cannot exceed 3000 characters")
    private String feedback;

    @Size(max = 20, message = "Outcome cannot exceed 20 characters")
    private String outcome;
}