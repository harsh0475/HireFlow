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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Interview Management",
        description = "Endpoints for scheduling, managing and viewing interviews."
)
@SecurityRequirement(name = "Bearer Authentication")
public class InterviewController {

    private final InterviewService interviewService;

    @Operation(
            summary = "Schedule interview",
            description = "Schedules a new interview for a job application. Accessible to recruiters and administrators."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Interview scheduled successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<InterviewResponse>> scheduleInterview(
            @Valid @RequestBody InterviewRequest request,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        boolean isAdmin = principal.getUser().getRole() == Role.ADMIN;

        InterviewResponse interview = interviewService.scheduleInterview(
                request,
                principal.getUser().getId(),
                isAdmin);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<InterviewResponse>builder()
                        .success(true)
                        .message("Interview scheduled successfully.")
                        .data(interview)
                        .build());
    }

    @Operation(
            summary = "Reschedule interview",
            description = "Updates the interview date, time or other scheduling information."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Interview rescheduled successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Interview not found", content = @Content)
    })
    @PutMapping("/{id}/reschedule")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<InterviewResponse>> rescheduleInterview(
            @Parameter(description = "Interview ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody InterviewUpdateRequest request,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        boolean isAdmin = principal.getUser().getRole() == Role.ADMIN;

        InterviewResponse interview = interviewService.rescheduleInterview(
                id,
                request,
                principal.getUser().getId(),
                isAdmin);

        return ResponseEntity.ok(
                ApiResponse.<InterviewResponse>builder()
                        .success(true)
                        .message("Interview rescheduled successfully.")
                        .data(interview)
                        .build()
        );
    }

    @Operation(
            summary = "Submit interview feedback",
            description = "Stores recruiter or administrator feedback for a completed interview."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Interview feedback submitted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Interview not found", content = @Content)
    })
    @PutMapping("/{id}/feedback")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<InterviewResponse>> submitFeedback(
            @Parameter(description = "Interview ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody InterviewFeedbackRequest request,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        boolean isAdmin = principal.getUser().getRole() == Role.ADMIN;

        InterviewResponse interview = interviewService.submitFeedback(
                id,
                request,
                principal.getUser().getId(),
                isAdmin);

        return ResponseEntity.ok(
                ApiResponse.<InterviewResponse>builder()
                        .success(true)
                        .message("Interview feedback submitted successfully.")
                        .data(interview)
                        .build()
        );
    }

    @Operation(
            summary = "Cancel interview",
            description = "Cancels a scheduled interview."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Interview cancelled successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Interview not found", content = @Content)
    })
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<InterviewResponse>> cancelInterview(
            @Parameter(description = "Interview ID", example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        boolean isAdmin = principal.getUser().getRole() == Role.ADMIN;

        InterviewResponse interview = interviewService.cancelInterview(
                id,
                principal.getUser().getId(),
                isAdmin);

        return ResponseEntity.ok(
                ApiResponse.<InterviewResponse>builder()
                        .success(true)
                        .message("Interview cancelled successfully.")
                        .data(interview)
                        .build()
        );
    }

    @Operation(
            summary = "Get interviews for an application",
            description = "Returns all interviews associated with a specific job application."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Interviews retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Application not found", content = @Content)
    })
    @GetMapping("/application/{applicationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<InterviewResponse>>> getInterviewsForApplication(
            @Parameter(description = "Application ID", example = "1")
            @PathVariable Long applicationId) {

        List<InterviewResponse> interviews =
                interviewService.getInterviewsForApplication(applicationId);

        return ResponseEntity.ok(
                ApiResponse.<List<InterviewResponse>>builder()
                        .success(true)
                        .message("Interviews fetched successfully.")
                        .data(interviews)
                        .build()
        );
    }

    @Operation(
            summary = "Get my interviews",
            description = "Returns a paginated list of interviews for the authenticated candidate."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Interviews retrieved successfully")
    })
    @GetMapping("/my-interviews")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<PageResponse<InterviewResponse>>> getMyInterviews(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal,
            @Parameter(description = "Pagination and sorting information")
            @PageableDefault(size = 10, sort = "interviewDate")
            Pageable pageable) {

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

    @Operation(
            summary = "Get recruiter interviews",
            description = "Returns all interviews associated with jobs managed by the authenticated recruiter."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Interviews retrieved successfully")
    })
    @GetMapping("/recruiter")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<PageResponse<InterviewResponse>>> getInterviewsForRecruiter(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal,
            @Parameter(description = "Pagination and sorting information")
            @PageableDefault(size = 10, sort = "interviewDate")
            Pageable pageable) {

        PageResponse<InterviewResponse> interviews =
                interviewService.getInterviewsForRecruiter(
                        principal.getUser().getId(),
                        pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<InterviewResponse>>builder()
                        .success(true)
                        .message("Interviews fetched successfully.")
                        .data(interviews)
                        .build()
        );
    }
}