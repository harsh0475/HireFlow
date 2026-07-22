package com.hireflow.controller;

import com.hireflow.dto.request.RecruiterProfileRequest;
import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.dto.response.RecruiterProfileResponse;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.RecruiterProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter-profiles")
@RequiredArgsConstructor
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<RecruiterProfileResponse>> createOrUpdateMyProfile(
            @Valid @RequestBody RecruiterProfileRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        RecruiterProfileResponse profile = recruiterProfileService.createOrUpdateProfile(
                request, principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<RecruiterProfileResponse>builder()
                        .success(true)
                        .message("Recruiter profile saved successfully.")
                        .data(profile)
                        .build()
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<RecruiterProfileResponse>> getMyProfile(
            @AuthenticationPrincipal UserPrincipal principal) {

        RecruiterProfileResponse profile = recruiterProfileService.getMyProfile(principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<RecruiterProfileResponse>builder()
                        .success(true)
                        .message("Recruiter profile fetched successfully.")
                        .data(profile)
                        .build()
        );
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<RecruiterProfileResponse>> getProfileByUserId(
            @PathVariable Long userId) {

        RecruiterProfileResponse profile = recruiterProfileService.getProfileByUserId(userId);

        return ResponseEntity.ok(
                ApiResponse.<RecruiterProfileResponse>builder()
                        .success(true)
                        .message("Recruiter profile fetched successfully.")
                        .data(profile)
                        .build()
        );
    }

    // ---- NEW: View Recruiters ----
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PageResponse<RecruiterProfileResponse>>> getAllRecruiters(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        PageResponse<RecruiterProfileResponse> recruiters = recruiterProfileService.getAllRecruiters(pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<RecruiterProfileResponse>>builder()
                        .success(true)
                        .message("Recruiters fetched successfully.")
                        .data(recruiters)
                        .build()
        );
    }
}