package com.hireflow.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruiterProfileRequest {

    private Long companyId;

    @Size(max = 150, message = "Designation cannot exceed 150 characters")
    private String designation;

    @Pattern(regexp = "^$|^[+]?[0-9\\-\\s]{7,20}$", message = "Invalid phone number")
    private String phone;

    @Size(max = 255, message = "LinkedIn URL cannot exceed 255 characters")
    private String linkedIn;
}