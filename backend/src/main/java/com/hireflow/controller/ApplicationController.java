package com.hireflow.controller;

import com.hireflow.dto.request.ApplicationRequest;
import com.hireflow.dto.request.ApplicationStatusUpdateRequest;
import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.ApplicationResponse;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.entity.enums.Role;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.ApplicationService;
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
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Tag(
        name = "Application Management",
        description = "Endpoints for candidates to apply for jobs and for recruiters/admins to manage job applications."
)
@SecurityRequirement(name = "Bearer Authentication")
public class ApplicationController {

    private final ApplicationService applicationService;

    @Operation(
            summary = "Apply for a job",
            description = "Allows an authenticated candidate to submit an application for a job."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Application submitted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Only candidates can apply", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<ApplicationResponse>> applyToJob(
            @Valid @RequestBody ApplicationRequest request,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        ApplicationResponse application =
                applicationService.applyToJob(request, principal.getUser().getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ApplicationResponse>builder()
                        .success(true)
                        .message("Application submitted successfully.")
                        .data(application)
                        .build());
    }

    @Operation(
            summary = "Withdraw application",
            description = "Allows a candidate to withdraw one of their submitted job applications."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Application withdrawn successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Application not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Only the application owner can withdraw", content = @Content)
    })
    @PutMapping("/{id}/withdraw")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<Void>> withdrawApplication(
            @Parameter(description = "Application ID", example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        applicationService.withdrawApplication(id, principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Application withdrawn successfully.")
                        .build()
        );
    }

    @Operation(
            summary = "Update application status",
            description = "Allows a recruiter or administrator to update the status of a job application."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Application status updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid status transition", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Application not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<ApplicationResponse>> updateStatus(
            @Parameter(description = "Application ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusUpdateRequest request,
            @Parameter(hidden = true)
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

    @Operation(
            summary = "Get application by ID",
            description = "Returns complete details of a specific application."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Application retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Application not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ApplicationResponse>> getApplication(
            @Parameter(description = "Application ID", example = "1")
            @PathVariable Long id) {

        ApplicationResponse application = applicationService.getApplicationById(id);

        return ResponseEntity.ok(
                ApiResponse.<ApplicationResponse>builder()
                        .success(true)
                        .message("Application fetched successfully.")
                        .data(application)
                        .build()
        );
    }

    @Operation(
            summary = "Get my applications",
            description = "Returns a paginated list of applications submitted by the authenticated candidate."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Applications retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<PageResponse<ApplicationResponse>>> getMyApplications(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal,
            @Parameter(description = "Pagination and sorting information")
            @PageableDefault(size = 10, sort = "createdAt")
            Pageable pageable) {

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

    @Operation(
            summary = "Get applications for a job",
            description = "Returns all applications submitted for a specific job. Accessible to recruiters who own the job and administrators."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Applications retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Job not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<PageResponse<ApplicationResponse>>> getApplicationsForJob(
            @Parameter(description = "Job ID", example = "1")
            @PathVariable Long jobId,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal,
            @Parameter(description = "Pagination and sorting information")
            @PageableDefault(size = 10, sort = "createdAt")
            Pageable pageable) {

        boolean isAdmin = principal.getUser().getRole() == Role.ADMIN;

        PageResponse<ApplicationResponse> applications =
                applicationService.getApplicationsForJob(
                        jobId,
                        principal.getUser().getId(),
                        isAdmin,
                        pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<ApplicationResponse>>builder()
                        .success(true)
                        .message("Applications fetched successfully.")
                        .data(applications)
                        .build()
        );
    }

    @Operation(
            summary = "Get recruiter applications",
            description = "Returns all applications received for jobs owned by the authenticated recruiter."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Applications retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @GetMapping("/recruiter")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<PageResponse<ApplicationResponse>>> getApplicationsForRecruiter(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal,
            @Parameter(description = "Pagination and sorting information")
            @PageableDefault(size = 10, sort = "createdAt")
            Pageable pageable) {

        PageResponse<ApplicationResponse> applications =
                applicationService.getApplicationsForRecruiter(
                        principal.getUser().getId(),
                        pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<ApplicationResponse>>builder()
                        .success(true)
                        .message("Applications fetched successfully.")
                        .data(applications)
                        .build()
        );
    }
}