package com.hireflow.controller;

import com.hireflow.dto.response.AdminDashboardResponse;
import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.CandidateDashboardResponse;
import com.hireflow.dto.response.RecruiterDashboardResponse;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(
        name = "Dashboard",
        description = "Role-based dashboard endpoints for candidates, recruiters, and administrators."
)
@SecurityRequirement(name = "Bearer Authentication")
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(
            summary = "Candidate dashboard",
            description = "Returns personalized dashboard statistics for the authenticated candidate."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Candidate dashboard retrieved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content
            )
    })
    @GetMapping("/candidate")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<CandidateDashboardResponse>> getCandidateDashboard(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        CandidateDashboardResponse dashboard =
                dashboardService.getCandidateDashboard(principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<CandidateDashboardResponse>builder()
                        .success(true)
                        .message("Candidate dashboard fetched successfully.")
                        .data(dashboard)
                        .build()
        );
    }

    @Operation(
            summary = "Recruiter dashboard",
            description = "Returns dashboard metrics for the authenticated recruiter. Administrators may also access this endpoint."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Recruiter dashboard retrieved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content
            )
    })
    @GetMapping("/recruiter")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<RecruiterDashboardResponse>> getRecruiterDashboard(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        RecruiterDashboardResponse dashboard =
                dashboardService.getRecruiterDashboard(principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<RecruiterDashboardResponse>builder()
                        .success(true)
                        .message("Recruiter dashboard fetched successfully.")
                        .data(dashboard)
                        .build()
        );
    }

    @Operation(
            summary = "Admin dashboard",
            description = "Returns platform-wide dashboard statistics for administrators."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Admin dashboard retrieved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content
            )
    })
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getAdminDashboard() {

        AdminDashboardResponse dashboard = dashboardService.getAdminDashboard();

        return ResponseEntity.ok(
                ApiResponse.<AdminDashboardResponse>builder()
                        .success(true)
                        .message("Admin dashboard fetched successfully.")
                        .data(dashboard)
                        .build()
        );
    }
}