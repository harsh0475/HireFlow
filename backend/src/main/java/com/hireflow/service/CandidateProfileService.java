package com.hireflow.service;

import com.hireflow.dto.request.CandidateProfileRequest;
import com.hireflow.dto.response.CandidateProfileResponse;

public interface CandidateProfileService {

    CandidateProfileResponse createOrUpdateProfile(CandidateProfileRequest request, Long userId);

    CandidateProfileResponse getMyProfile(Long userId);

    CandidateProfileResponse getProfileByUserId(Long userId);
}