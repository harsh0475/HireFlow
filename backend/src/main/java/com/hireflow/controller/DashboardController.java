package com.hireflow.controller;

import com.hireflow.dto.response.AdminDashboardResponse;
import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.CandidateDashboardResponse;
import com.hireflow.dto.response.RecruiterDashboardResponse;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.DashboardService;
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
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/candidate")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<CandidateDashboardResponse>> getCandidateDashboard(
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

    @GetMapping("/recruiter")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<RecruiterDashboardResponse>> getRecruiterDashboard(
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