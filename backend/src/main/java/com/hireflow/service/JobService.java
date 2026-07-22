package com.hireflow.service;

import com.hireflow.dto.request.JobFilterRequest;
import com.hireflow.dto.request.JobRequest;
import com.hireflow.dto.response.JobResponse;
import com.hireflow.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface JobService {

    JobResponse createJob(JobRequest request, Long recruiterId);

    JobResponse updateJob(Long jobId, JobRequest request, Long recruiterId, boolean isAdmin);

    JobResponse getJobById(Long jobId);

    PageResponse<JobResponse> getAllJobs(JobFilterRequest filter, Pageable pageable);

    PageResponse<JobResponse> getJobsByCompany(Long companyId, Pageable pageable);

    PageResponse<JobResponse> getJobsByRecruiter(Long recruiterId, Pageable pageable);

    void deleteJob(Long jobId, Long recruiterId, boolean isAdmin);
}