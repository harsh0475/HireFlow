package com.hireflow.service.impl;

import com.hireflow.dto.request.InterviewFeedbackRequest;
import com.hireflow.dto.request.InterviewRequest;
import com.hireflow.dto.request.InterviewUpdateRequest;
import com.hireflow.dto.response.InterviewResponse;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.entity.Application;
import com.hireflow.entity.Interview;
import com.hireflow.entity.enums.ApplicationStatus;
import com.hireflow.entity.enums.InterviewStatus;
import com.hireflow.exception.BadRequestException;
import com.hireflow.exception.ResourceNotFoundException;
import com.hireflow.exception.UnauthorizedException;
import com.hireflow.mapper.InterviewMapper;
import com.hireflow.repository.ApplicationRepository;
import com.hireflow.repository.InterviewRepository;
import com.hireflow.service.InterviewService;
import com.hireflow.specification.InterviewSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewMapper interviewMapper;

    @Override
    @Transactional
    public InterviewResponse scheduleInterview(InterviewRequest request, Long recruiterId, boolean isAdmin) {

        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Application not found."));

        if (!isAdmin && !application.getJob().getCreatedBy().getId().equals(recruiterId)) {
            throw new UnauthorizedException("You are not allowed to schedule interviews for this application.");
        }

        if (application.getStatus() == ApplicationStatus.WITHDRAWN
                || application.getStatus() == ApplicationStatus.REJECTED) {
            throw new BadRequestException("Cannot schedule an interview for this application.");
        }

        Interview interview = interviewMapper.toEntity(request);
        interview.setApplication(application);
        interview.setStatus(InterviewStatus.SCHEDULED);

        interview = interviewRepository.save(interview);

        application.setStatus(ApplicationStatus.INTERVIEW_SCHEDULED);
        applicationRepository.save(application);

        return interviewMapper.toResponse(interview);
    }

    @Override
    @Transactional
    public InterviewResponse rescheduleInterview(
            Long interviewId,
            InterviewUpdateRequest request,
            Long recruiterId,
            boolean isAdmin) {

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Interview not found."));

        assertOwnership(interview, recruiterId, isAdmin);

        interviewMapper.updateInterviewFromRequest(request, interview);
        interview.setStatus(InterviewStatus.RESCHEDULED);

        interview = interviewRepository.save(interview);

        return interviewMapper.toResponse(interview);
    }

    @Override
    @Transactional
    public InterviewResponse submitFeedback(
            Long interviewId,
            InterviewFeedbackRequest request,
            Long recruiterId,
            boolean isAdmin) {

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Interview not found."));

        assertOwnership(interview, recruiterId, isAdmin);

        interview.setFeedback(request.getFeedback());
        interview.setOutcome(request.getOutcome());
        interview.setStatus(InterviewStatus.COMPLETED);

        interview = interviewRepository.save(interview);

        return interviewMapper.toResponse(interview);
    }

    @Override
    @Transactional
    public InterviewResponse cancelInterview(Long interviewId, Long recruiterId, boolean isAdmin) {

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Interview not found."));

        assertOwnership(interview, recruiterId, isAdmin);

        interview.setStatus(InterviewStatus.CANCELLED);

        interview = interviewRepository.save(interview);

        return interviewMapper.toResponse(interview);
    }

    @Override
    public List<InterviewResponse> getInterviewsForApplication(Long applicationId) {

        return interviewRepository.findByApplicationId(applicationId)
                .stream()
                .map(interviewMapper::toResponse)
                .toList();
    }

    @Override
    public PageResponse<InterviewResponse> getMyInterviews(Long candidateId, Pageable pageable) {

        Specification<Interview> specification = InterviewSpecification.hasCandidate(candidateId);

        Page<InterviewResponse> page = interviewRepository.findAll(specification, pageable)
                .map(interviewMapper::toResponse);

        return PageResponse.from(page);
    }

    @Override
    public PageResponse<InterviewResponse> getInterviewsForRecruiter(Long recruiterId, Pageable pageable) {

        Specification<Interview> specification = InterviewSpecification.hasRecruiter(recruiterId);

        Page<InterviewResponse> page = interviewRepository.findAll(specification, pageable)
                .map(interviewMapper::toResponse);

        return PageResponse.from(page);
    }

    private void assertOwnership(Interview interview, Long recruiterId, boolean isAdmin) {

        if (!isAdmin && !interview.getApplication().getJob().getCreatedBy().getId().equals(recruiterId)) {
            throw new UnauthorizedException("You are not allowed to modify this interview.");
        }
    }
}