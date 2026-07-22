package com.hireflow.controller;

import com.hireflow.dto.request.CandidateProfileRequest;
import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.CandidateProfileResponse;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.CandidateProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidate-profiles")
@RequiredArgsConstructor
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;

    @PutMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> createOrUpdateMyProfile(
            @Valid @RequestBody CandidateProfileRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        CandidateProfileResponse profile = candidateProfileService.createOrUpdateProfile(
                request, principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<CandidateProfileResponse>builder()
                        .success(true)
                        .message("Candidate profile saved successfully.")
                        .data(profile)
                        .build()
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> getMyProfile(
            @AuthenticationPrincipal UserPrincipal principal) {

        CandidateProfileResponse profile = candidateProfileService.getMyProfile(principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<CandidateProfileResponse>builder()
                        .success(true)
                        .message("Candidate profile fetched successfully.")
                        .data(profile)
                        .build()
        );
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> getProfileByUserId(
            @PathVariable Long userId) {

        CandidateProfileResponse profile = candidateProfileService.getProfileByUserId(userId);

        return ResponseEntity.ok(
                ApiResponse.<CandidateProfileResponse>builder()
                        .success(true)
                        .message("Candidate profile fetched successfully.")
                        .data(profile)
                        .build()
        );
    }
}