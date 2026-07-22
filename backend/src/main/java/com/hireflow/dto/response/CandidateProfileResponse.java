package com.hireflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileResponse {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String resumeUrl;
    private String profilePictureUrl;
    private Set<String> skills;
    private String experience;
    private String education;
    private String linkedIn;
    private String portfolio;
    private String currentCompany;
    private Double currentCtc;
    private Double expectedCtc;
    private String noticePeriod;
    private String location;
}