package com.hireflow.controller;

import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.dto.response.PlatformStatisticsResponse;
import com.hireflow.dto.response.UserResponse;
import com.hireflow.service.AdminService;
import com.hireflow.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final StatisticsService statisticsService;
    private final AdminService adminService;

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<PlatformStatisticsResponse>> getPlatformStatistics() {

        PlatformStatisticsResponse statistics =
                statisticsService.getPlatformStatistics();

        return ResponseEntity.ok(
                ApiResponse.<PlatformStatisticsResponse>builder()
                        .success(true)
                        .message("Platform statistics fetched successfully.")
                        .data(statistics)
                        .build()
        );
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<UserResponse>>builder()
                        .success(true)
                        .message("Users fetched successfully.")
                        .data(adminService.getAllUsers(pageable))
                        .build()
        );
    }

    @GetMapping("/users/candidates")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getCandidates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<UserResponse>>builder()
                        .success(true)
                        .message("Candidates fetched successfully.")
                        .data(adminService.getCandidates(pageable))
                        .build()
        );
    }

    @GetMapping("/users/recruiters")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getRecruiters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<UserResponse>>builder()
                        .success(true)
                        .message("Recruiters fetched successfully.")
                        .data(adminService.getRecruiters(pageable))
                        .build()
        );
    }

    @GetMapping("/users/admins")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAdmins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<UserResponse>>builder()
                        .success(true)
                        .message("Admins fetched successfully.")
                        .data(adminService.getAdmins(pageable))
                        .build()
        );
    }

    @PatchMapping("/users/{id}/activate")
    public ResponseEntity<ApiResponse<UserResponse>> activateUser(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("User activated successfully.")
                        .data(adminService.activateUser(id))
                        .build()
        );
    }

    @PatchMapping("/users/{id}/deactivate")
    public ResponseEntity<ApiResponse<UserResponse>> deactivateUser(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("User deactivated successfully.")
                        .data(adminService.deactivateUser(id))
                        .build()
        );
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id
    ) {

        adminService.deleteUser(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("User deleted successfully.")
                        .build()
        );
    }
}