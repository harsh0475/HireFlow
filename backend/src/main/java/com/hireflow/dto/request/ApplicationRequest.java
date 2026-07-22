package com.hireflow.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationRequest {

    @NotNull(message = "Job id is required")
    private Long jobId;

    @Size(max = 3000, message = "Cover letter cannot exceed 3000 characters")
    private String coverLetter;

    @Size(max = 255, message = "Resume URL cannot exceed 255 characters")
    private String resumeUrl;
}