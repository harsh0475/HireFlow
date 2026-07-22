package com.hireflow.controller;

import com.hireflow.dto.request.JobFilterRequest;
import com.hireflow.dto.request.JobRequest;
import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.JobResponse;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.entity.enums.Role;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.JobService;
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

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Tag(
        name = "Job Management",
        description = "Endpoints for creating, updating, searching and managing job postings."
)
@SecurityRequirement(name = "Bearer Authentication")
public class JobController {

    private final JobService jobService;

    @Operation(
            summary = "Create a job",
            description = "Creates a new job posting. Accessible to ADMIN and RECRUITER roles."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Job created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<JobResponse>> createJob(
            @Valid @RequestBody JobRequest request,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        JobResponse job = jobService.createJob(request, principal.getUser().getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<JobResponse>builder()
                        .success(true)
                        .message("Job created successfully.")
                        .data(job)
                        .build());
    }

    @Operation(
            summary = "Update a job",
            description = "Updates an existing job posting. Recruiters can update their own jobs, while admins can update any job."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Job updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Job not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(
            @Parameter(description = "Unique job ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody JobRequest request,
            @Parameter(hidden = true)
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

    @Operation(
            summary = "Get job by ID",
            description = "Returns complete details of a specific job posting."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Job retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Job not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<JobResponse>> getJob(
            @Parameter(description = "Unique job ID", example = "1")
            @PathVariable Long id) {

        JobResponse job = jobService.getJobById(id);

        return ResponseEntity.ok(
                ApiResponse.<JobResponse>builder()
                        .success(true)
                        .message("Job fetched successfully.")
                        .data(job)
                        .build()
        );
    }

    @Operation(
            summary = "Search jobs",
            description = "Returns a paginated list of jobs using the supplied filter criteria."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Jobs retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PageResponse<JobResponse>>> getAllJobs(
            @Parameter(description = "Job search filters")
            JobFilterRequest filter,
            @Parameter(description = "Pagination and sorting information")
            @PageableDefault(size = 10, sort = "createdAt")
            Pageable pageable) {

        PageResponse<JobResponse> jobs = jobService.getAllJobs(filter, pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<JobResponse>>builder()
                        .success(true)
                        .message("Jobs fetched successfully.")
                        .data(jobs)
                        .build()
        );
    }

    @Operation(
            summary = "Get jobs by company",
            description = "Returns all job postings for a specific company."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Jobs retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Company not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/company/{companyId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PageResponse<JobResponse>>> getJobsByCompany(
            @Parameter(description = "Unique company ID", example = "1")
            @PathVariable Long companyId,
            @Parameter(description = "Pagination and sorting information")
            @PageableDefault(size = 10, sort = "createdAt")
            Pageable pageable) {

        PageResponse<JobResponse> jobs = jobService.getJobsByCompany(companyId, pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<JobResponse>>builder()
                        .success(true)
                        .message("Jobs fetched successfully.")
                        .data(jobs)
                        .build()
        );
    }

    @Operation(
            summary = "Get my jobs",
            description = "Returns all job postings created by the authenticated recruiter. Admins may also access this endpoint."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Jobs retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @GetMapping("/my-jobs")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<PageResponse<JobResponse>>> getMyJobs(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal,
            @Parameter(description = "Pagination and sorting information")
            @PageableDefault(size = 10, sort = "createdAt")
            Pageable pageable) {

        PageResponse<JobResponse> jobs = jobService.getJobsByRecruiter(principal.getUser().getId(), pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<JobResponse>>builder()
                        .success(true)
                        .message("Jobs fetched successfully.")
                        .data(jobs)
                        .build()
        );
    }

    @Operation(
            summary = "Delete a job",
            description = "Deletes a job posting. Recruiters may delete their own jobs, while admins may delete any job."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Job deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Job not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<Void>> deleteJob(
            @Parameter(description = "Unique job ID", example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true)
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