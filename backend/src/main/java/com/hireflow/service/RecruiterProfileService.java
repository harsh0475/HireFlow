package com.hireflow.service;

import com.hireflow.dto.request.RecruiterProfileRequest;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.dto.response.RecruiterProfileResponse;
import org.springframework.data.domain.Pageable;

public interface RecruiterProfileService {

    RecruiterProfileResponse createOrUpdateProfile(RecruiterProfileRequest request, Long userId);

    RecruiterProfileResponse getMyProfile(Long userId);

    RecruiterProfileResponse getProfileByUserId(Long userId);

    PageResponse<RecruiterProfileResponse> getAllRecruiters(Pageable pageable);
}