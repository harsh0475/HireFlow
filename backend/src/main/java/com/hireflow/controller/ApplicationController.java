package com.hireflow.controller;

import com.hireflow.dto.request.ApplicationRequest;
import com.hireflow.dto.request.ApplicationStatusUpdateRequest;
import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.ApplicationResponse;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.entity.enums.Role;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<ApplicationResponse>> applyToJob(
            @Valid @RequestBody ApplicationRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        ApplicationResponse application = applicationService.applyToJob(request, principal.getUser().getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ApplicationResponse>builder()
                        .success(true)
                        .message("Application submitted successfully.")
                        .data(application)
                        .build());
    }

    @PutMapping("/{id}/withdraw")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<Void>> withdrawApplication(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {

        applicationService.withdrawApplication(id, principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Application withdrawn successfully.")
                        .build()
        );
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<ApplicationResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        boolean isAdmin = principal.getUser().getRole() == Role.ADMIN;

        ApplicationResponse application = applicationService.updateStatus(
                id, request, principal.getUser().getId(), isAdmin);

        return ResponseEntity.ok(
                ApiResponse.<ApplicationResponse>builder()
                        .success(true)
                        .message("Application status updated successfully.")
                        .data(application)
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ApplicationResponse>> getApplication(@PathVariable Long id) {

        ApplicationResponse application = applicationService.getApplicationById(id);

        return ResponseEntity.ok(
                ApiResponse.<ApplicationResponse>builder()
                        .success(true)
                        .message("Application fetched successfully.")
                        .data(application)
                        .build()
        );
    }

    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<PageResponse<ApplicationResponse>>> getMyApplications(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        PageResponse<ApplicationResponse> applications =
                applicationService.getMyApplications(principal.getUser().getId(), pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<ApplicationResponse>>builder()
                        .success(true)
                        .message("Applications fetched successfully.")
                        .data(applications)
                        .build()
        );
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<PageResponse<ApplicationResponse>>> getApplicationsForJob(
            @PathVariable Long jobId,
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        boolean isAdmin = principal.getUser().getRole() == Role.ADMIN;

        PageResponse<ApplicationResponse> applications = applicationService.getApplicationsForJob(
                jobId, principal.getUser().getId(), isAdmin, pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<ApplicationResponse>>builder()
                        .success(true)
                        .message("Applications fetched successfully.")
                        .data(applications)
                        .build()
        );
    }

    @GetMapping("/recruiter")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<PageResponse<ApplicationResponse>>> getApplicationsForRecruiter(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        PageResponse<ApplicationResponse> applications =
                applicationService.getApplicationsForRecruiter(principal.getUser().getId(), pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<ApplicationResponse>>builder()
                        .success(true)
                        .message("Applications fetched successfully.")
                        .data(applications)
                        .build()
        );
    }
}