package com.hireflow.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RecruiterProfileResponse {

    private Long id;

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private CompanyResponse company;

    private String designation;

    private String phone;

    private String linkedIn;
}