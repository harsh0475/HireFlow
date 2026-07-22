package com.hireflow.dto.request;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CandidateProfileRequest {

    @Size(max = 255, message = "Resume URL cannot exceed 255 characters")
    private String resumeUrl;

    private Set<String> skills;

    private String experience;

    private String education;

    @Size(max = 255, message = "LinkedIn URL cannot exceed 255 characters")
    private String linkedIn;

    @Size(max = 255, message = "Portfolio URL cannot exceed 255 characters")
    private String portfolio;

    @Size(max = 150, message = "Current company cannot exceed 150 characters")
    private String currentCompany;

    @PositiveOrZero(message = "Current CTC cannot be negative")
    private Double currentCtc;

    @PositiveOrZero(message = "Expected CTC cannot be negative")
    private Double expectedCtc;

    @Size(max = 50, message = "Notice period cannot exceed 50 characters")
    private String noticePeriod;

    @Size(max = 150, message = "Location cannot exceed 150 characters")
    private String location;
}