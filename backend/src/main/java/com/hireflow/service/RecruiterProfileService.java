package com.hireflow.service;

import com.hireflow.dto.request.RecruiterProfileRequest;
import com.hireflow.dto.response.RecruiterProfileResponse;

public interface RecruiterProfileService {

    RecruiterProfileResponse createOrUpdateProfile(RecruiterProfileRequest request, Long userId);

    RecruiterProfileResponse getMyProfile(Long userId);

    RecruiterProfileResponse getProfileByUserId(Long userId);
}