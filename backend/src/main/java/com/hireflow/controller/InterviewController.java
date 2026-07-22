package com.hireflow.controller;

import com.hireflow.dto.request.InterviewFeedbackRequest;
import com.hireflow.dto.request.InterviewRequest;
import com.hireflow.dto.request.InterviewUpdateRequest;
import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.InterviewResponse;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.entity.enums.Role;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<InterviewResponse>> scheduleInterview(
            @Valid @RequestBody InterviewRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        boolean isAdmin = principal.getUser().getRole() == Role.ADMIN;

        InterviewResponse interview = interviewService.scheduleInterview(
                request, principal.getUser().getId(), isAdmin);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<InterviewResponse>builder()
                        .success(true)
                        .message("Interview scheduled successfully.")
                        .data(interview)
                        .build());
    }

    @PutMapping("/{id}/reschedule")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<InterviewResponse>> rescheduleInterview(
            @PathVariable Long id,
            @Valid @RequestBody InterviewUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        boolean isAdmin = principal.getUser().getRole() == Role.ADMIN;

        InterviewResponse interview = interviewService.rescheduleInterview(
                id, request, principal.getUser().getId(), isAdmin);

        return ResponseEntity.ok(
                ApiResponse.<InterviewResponse>builder()
                        .success(true)
                        .message("Interview rescheduled successfully.")
                        .data(interview)
                        .build()
        );
    }

    @PutMapping("/{id}/feedback")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<InterviewResponse>> submitFeedback(
            @PathVariable Long id,
            @Valid @RequestBody InterviewFeedbackRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        boolean isAdmin = principal.getUser().getRole() == Role.ADMIN;

        InterviewResponse interview = interviewService.submitFeedback(
                id, request, principal.getUser().getId(), isAdmin);

        return ResponseEntity.ok(
                ApiResponse.<InterviewResponse>builder()
                        .success(true)
                        .message("Interview feedback submitted successfully.")
                        .data(interview)
                        .build()
        );
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<InterviewResponse>> cancelInterview(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {

        boolean isAdmin = principal.getUser().getRole() == Role.ADMIN;

        InterviewResponse interview = interviewService.cancelInterview(
                id, principal.getUser().getId(), isAdmin);

        return ResponseEntity.ok(
                ApiResponse.<InterviewResponse>builder()
                        .success(true)
                        .message("Interview cancelled successfully.")
                        .data(interview)
                        .build()
        );
    }

    @GetMapping("/application/{applicationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<InterviewResponse>>> getInterviewsForApplication(
            @PathVariable Long applicationId) {

        List<InterviewResponse> interviews = interviewService.getInterviewsForApplication(applicationId);

        return ResponseEntity.ok(
                ApiResponse.<List<InterviewResponse>>builder()
                        .success(true)
                        .message("Interviews fetched successfully.")
                        .data(interviews)
                        .build()
        );
    }

    @GetMapping("/my-interviews")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<PageResponse<InterviewResponse>>> getMyInterviews(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 10, sort = "interviewDate") Pageable pageable) {

        PageResponse<InterviewResponse> interviews =
                interviewService.getMyInterviews(principal.getUser().getId(), pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<InterviewResponse>>builder()
                        .success(true)
                        .message("Interviews fetched successfully.")
                        .data(interviews)
                        .build()
        );
    }

    @GetMapping("/recruiter")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<PageResponse<InterviewResponse>>> getInterviewsForRecruiter(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 10, sort = "interviewDate") Pageable pageable) {

        PageResponse<InterviewResponse> interviews =
                interviewService.getInterviewsForRecruiter(principal.getUser().getId(), pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<InterviewResponse>>builder()
                        .success(true)
                        .message("Interviews fetched successfully.")
                        .data(interviews)
                        .build()
        );
    }
}