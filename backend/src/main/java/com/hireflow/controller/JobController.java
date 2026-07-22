package com.hireflow.controller;

import com.hireflow.dto.request.JobFilterRequest;
import com.hireflow.dto.request.JobRequest;
import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.JobResponse;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.entity.enums.Role;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.JobService;
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
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<JobResponse>> createJob(
            @Valid @RequestBody JobRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        JobResponse job = jobService.createJob(request, principal.getUser().getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<JobResponse>builder()
                        .success(true)
                        .message("Job created successfully.")
                        .data(job)
                        .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        boolean isAdmin = principal.getUser().getRole() == Role.ADMIN;

        JobResponse job = jobService.updateJob(id, request, principal.getUser().getId(), isAdmin);

        return ResponseEntity.ok(
                ApiResponse.<JobResponse>builder()
                        .success(true)
                        .message("Job updated successfully.")
                        .data(job)
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<JobResponse>> getJob(@PathVariable Long id) {

        JobResponse job = jobService.getJobById(id);

        return ResponseEntity.ok(
                ApiResponse.<JobResponse>builder()
                        .success(true)
                        .message("Job fetched successfully.")
                        .data(job)
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PageResponse<JobResponse>>> getAllJobs(
            JobFilterRequest filter,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        PageResponse<JobResponse> jobs = jobService.getAllJobs(filter, pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<JobResponse>>builder()
                        .success(true)
                        .message("Jobs fetched successfully.")
                        .data(jobs)
                        .build()
        );
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PageResponse<JobResponse>>> getJobsByCompany(
            @PathVariable Long companyId,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        PageResponse<JobResponse> jobs = jobService.getJobsByCompany(companyId, pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<JobResponse>>builder()
                        .success(true)
                        .message("Jobs fetched successfully.")
                        .data(jobs)
                        .build()
        );
    }

    @GetMapping("/my-jobs")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<PageResponse<JobResponse>>> getMyJobs(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        PageResponse<JobResponse> jobs = jobService.getJobsByRecruiter(principal.getUser().getId(), pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<JobResponse>>builder()
                        .success(true)
                        .message("Jobs fetched successfully.")
                        .data(jobs)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<Void>> deleteJob(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {

        boolean isAdmin = principal.getUser().getRole() == Role.ADMIN;

        jobService.deleteJob(id, principal.getUser().getId(), isAdmin);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Job deleted successfully.")
                        .build()
        );
    }
}