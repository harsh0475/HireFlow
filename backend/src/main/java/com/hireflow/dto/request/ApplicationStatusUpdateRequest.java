package com.hireflow.dto.request;

import com.hireflow.entity.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private ApplicationStatus status;

    @Size(max = 2000, message = "Recruiter notes cannot exceed 2000 characters")
    private String recruiterNotes;
}