package com.hireflow.service;

import com.hireflow.dto.request.ApplicationRequest;
import com.hireflow.dto.request.ApplicationStatusUpdateRequest;
import com.hireflow.dto.response.ApplicationResponse;
import com.hireflow.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface ApplicationService {

    ApplicationResponse applyToJob(ApplicationRequest request, Long candidateId);

    void withdrawApplication(Long applicationId, Long candidateId);

    ApplicationResponse updateStatus(Long applicationId, ApplicationStatusUpdateRequest request, Long recruiterId, boolean isAdmin);

    ApplicationResponse getApplicationById(Long applicationId);

    PageResponse<ApplicationResponse> getMyApplications(Long candidateId, Pageable pageable);

    PageResponse<ApplicationResponse> getApplicationsForJob(Long jobId, Long recruiterId, boolean isAdmin, Pageable pageable);

    PageResponse<ApplicationResponse> getApplicationsForRecruiter(Long recruiterId, Pageable pageable);
}