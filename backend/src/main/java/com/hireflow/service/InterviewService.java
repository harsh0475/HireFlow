package com.hireflow.service;

import com.hireflow.dto.request.InterviewFeedbackRequest;
import com.hireflow.dto.request.InterviewRequest;
import com.hireflow.dto.request.InterviewUpdateRequest;
import com.hireflow.dto.response.InterviewResponse;
import com.hireflow.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InterviewService {

    InterviewResponse scheduleInterview(InterviewRequest request, Long recruiterId, boolean isAdmin);

    InterviewResponse rescheduleInterview(Long interviewId, InterviewUpdateRequest request, Long recruiterId, boolean isAdmin);

    InterviewResponse submitFeedback(Long interviewId, InterviewFeedbackRequest request, Long recruiterId, boolean isAdmin);

    InterviewResponse cancelInterview(Long interviewId, Long recruiterId, boolean isAdmin);

    List<InterviewResponse> getInterviewsForApplication(Long applicationId);

    PageResponse<InterviewResponse> getMyInterviews(Long candidateId, Pageable pageable);

    PageResponse<InterviewResponse> getInterviewsForRecruiter(Long recruiterId, Pageable pageable);
}