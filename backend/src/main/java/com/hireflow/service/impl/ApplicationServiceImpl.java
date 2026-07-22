package com.hireflow.service.impl;

import com.hireflow.dto.request.ApplicationRequest;
import com.hireflow.dto.request.ApplicationStatusUpdateRequest;
import com.hireflow.dto.response.ApplicationResponse;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.entity.Application;
import com.hireflow.entity.Job;
import com.hireflow.entity.User;
import com.hireflow.entity.enums.ApplicationStatus;
import com.hireflow.entity.enums.JobStatus;
import com.hireflow.exception.BadRequestException;
import com.hireflow.exception.ResourceNotFoundException;
import com.hireflow.exception.UnauthorizedException;
import com.hireflow.mapper.ApplicationMapper;
import com.hireflow.repository.ApplicationRepository;
import com.hireflow.repository.JobRepository;
import com.hireflow.repository.UserRepository;
import com.hireflow.service.ApplicationService;
import com.hireflow.specification.ApplicationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationMapper applicationMapper;

    @Override
    @Transactional
    public ApplicationResponse applyToJob(ApplicationRequest request, Long candidateId) {

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found."));

        if (job.getStatus() != JobStatus.PUBLISHED || !Boolean.TRUE.equals(job.getActive())) {
            throw new BadRequestException("This job is not open for applications.");
        }

        User candidate = userRepository.findById(candidateId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        if (applicationRepository.existsByJobIdAndCandidateIdAndActiveTrue(job.getId(), candidateId)) {
            throw new BadRequestException("You have already applied to this job.");
        }

        Application application = Application.builder()
                .job(job)
                .candidate(candidate)
                .coverLetter(request.getCoverLetter())
                .resumeUrl(request.getResumeUrl())
                .status(ApplicationStatus.APPLIED)
                .active(true)
                .build();

        application = applicationRepository.save(application);

        return applicationMapper.toResponse(application);
    }

    @Override
    @Transactional
    public void withdrawApplication(Long applicationId, Long candidateId) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Application not found."));

        if (!application.getCandidate().getId().equals(candidateId)) {
            throw new UnauthorizedException("You are not allowed to withdraw this application.");
        }

        if (application.getStatus() == ApplicationStatus.HIRED) {
            throw new BadRequestException("Cannot withdraw an application that has already been hired.");
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);
        application.setActive(false);

        applicationRepository.save(application);
    }

    @Override
    @Transactional
    public ApplicationResponse updateStatus(
            Long applicationId,
            ApplicationStatusUpdateRequest request,
            Long recruiterId,
            boolean isAdmin) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Application not found."));

        if (!isAdmin && !application.getJob().getCreatedBy().getId().equals(recruiterId)) {
            throw new UnauthorizedException("You are not allowed to update this application.");
        }

        if (application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new BadRequestException("Cannot update a withdrawn application.");
        }

        application.setStatus(request.getStatus());

        if (request.getRecruiterNotes() != null) {
            application.setRecruiterNotes(request.getRecruiterNotes());
        }

        application = applicationRepository.save(application);

        return applicationMapper.toResponse(application);
    }

    @Override
    public ApplicationResponse getApplicationById(Long applicationId) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Application not found."));

        return applicationMapper.toResponse(application);
    }

    @Override
    public PageResponse<ApplicationResponse> getMyApplications(Long candidateId, Pageable pageable) {

        Specification<Application> specification = ApplicationSpecification.hasCandidate(candidateId);

        Page<ApplicationResponse> page = applicationRepository.findAll(specification, pageable)
                .map(applicationMapper::toResponse);

        return PageResponse.from(page);
    }

    @Override
    public PageResponse<ApplicationResponse> getApplicationsForJob(
            Long jobId,
            Long recruiterId,
            boolean isAdmin,
            Pageable pageable) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found."));

        if (!isAdmin && !job.getCreatedBy().getId().equals(recruiterId)) {
            throw new UnauthorizedException("You are not allowed to view applications for this job.");
        }

        Specification<Application> specification = ApplicationSpecification.hasJob(jobId);

        Page<ApplicationResponse> page = applicationRepository.findAll(specification, pageable)
                .map(applicationMapper::toResponse);

        return PageResponse.from(page);
    }

    @Override
    public PageResponse<ApplicationResponse> getApplicationsForRecruiter(Long recruiterId, Pageable pageable) {

        Specification<Application> specification = ApplicationSpecification.hasRecruiter(recruiterId);

        Page<ApplicationResponse> page = applicationRepository.findAll(specification, pageable)
                .map(applicationMapper::toResponse);

        return PageResponse.from(page);
    }
}