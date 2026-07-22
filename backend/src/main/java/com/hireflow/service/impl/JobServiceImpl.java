package com.hireflow.service.impl;

import com.hireflow.dto.request.JobFilterRequest;
import com.hireflow.dto.request.JobRequest;
import com.hireflow.dto.response.JobResponse;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.entity.Company;
import com.hireflow.entity.Job;
import com.hireflow.entity.User;
import com.hireflow.entity.enums.JobStatus;
import com.hireflow.exception.ResourceNotFoundException;
import com.hireflow.exception.UnauthorizedException;
import com.hireflow.mapper.JobMapper;
import com.hireflow.repository.CompanyRepository;
import com.hireflow.repository.JobRepository;
import com.hireflow.repository.UserRepository;
import com.hireflow.service.JobService;
import com.hireflow.specification.JobSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final JobMapper jobMapper;

    @Override
    @Transactional
    public JobResponse createJob(JobRequest request, Long recruiterId) {

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Company not found."));

        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        Job job = jobMapper.toEntity(request);
        job.setCompany(company);
        job.setCreatedBy(recruiter);
        job.setStatus(JobStatus.DRAFT);
        job.setActive(true);

        if (job.getCurrency() == null) {
            job.setCurrency("INR");
        }

        job = jobRepository.save(job);

        return jobMapper.toResponse(job);
    }

    @Override
    @Transactional
    public JobResponse updateJob(Long jobId, JobRequest request, Long recruiterId, boolean isAdmin) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found."));

        if (!isAdmin && !job.getCreatedBy().getId().equals(recruiterId)) {
            throw new UnauthorizedException("You are not allowed to modify this job.");
        }

        if (request.getCompanyId() != null
                && !request.getCompanyId().equals(job.getCompany().getId())) {

            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Company not found."));

            job.setCompany(company);
        }

        jobMapper.updateJobFromRequest(request, job);

        job = jobRepository.save(job);

        return jobMapper.toResponse(job);
    }

    @Override
    public JobResponse getJobById(Long jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found."));

        return jobMapper.toResponse(job);
    }

    @Override
    public PageResponse<JobResponse> getAllJobs(JobFilterRequest filter, Pageable pageable) {

        Specification<Job> specification = JobSpecification.build(filter);

        Page<JobResponse> page = jobRepository.findAll(specification, pageable)
                .map(jobMapper::toResponse);

        return PageResponse.from(page);
    }

    @Override
    public PageResponse<JobResponse> getJobsByCompany(Long companyId, Pageable pageable) {

        Specification<Job> specification = JobSpecification.isActive()
                .and(JobSpecification.hasCompany(companyId));

        Page<JobResponse> page = jobRepository.findAll(specification, pageable)
                .map(jobMapper::toResponse);

        return PageResponse.from(page);
    }

    @Override
    public PageResponse<JobResponse> getJobsByRecruiter(Long recruiterId, Pageable pageable) {

        Specification<Job> specification = JobSpecification.hasCreatedBy(recruiterId);

        Page<JobResponse> page = jobRepository.findAll(specification, pageable)
                .map(jobMapper::toResponse);

        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public void deleteJob(Long jobId, Long recruiterId, boolean isAdmin) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found."));

        if (!isAdmin && !job.getCreatedBy().getId().equals(recruiterId)) {
            throw new UnauthorizedException("You are not allowed to delete this job.");
        }

        job.setActive(false);
        job.setStatus(JobStatus.ARCHIVED);

        jobRepository.save(job);
    }
}