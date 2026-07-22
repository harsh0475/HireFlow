package com.hireflow.controller;

import com.hireflow.dto.request.CandidateProfileRequest;
import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.CandidateProfileResponse;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.CandidateProfileService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidate-profiles")
@RequiredArgsConstructor
@Tag(
        name = "Candidate Profiles",
        description = "Endpoints for candidates to manage their profiles and for recruiters/admins to view candidate profiles."
)
@SecurityRequirement(name = "Bearer Authentication")
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;

    @Operation(
            summary = "Create or update my profile",
            description = "Creates a new candidate profile or updates the existing profile for the authenticated candidate."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Candidate profile saved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Only candidates can manage their profile", content = @Content)
    })
    @PutMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> createOrUpdateMyProfile(
            @Valid @RequestBody CandidateProfileRequest request,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        CandidateProfileResponse profile = candidateProfileService.createOrUpdateProfile(
                request,
                principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<CandidateProfileResponse>builder()
                        .success(true)
                        .message("Candidate profile saved successfully.")
                        .data(profile)
                        .build()
        );
    }

    @Operation(
            summary = "Get my profile",
            description = "Returns the profile of the authenticated candidate."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Candidate profile retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Profile not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> getMyProfile(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        CandidateProfileResponse profile =
                candidateProfileService.getMyProfile(principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<CandidateProfileResponse>builder()
                        .success(true)
                        .message("Candidate profile fetched successfully.")
                        .data(profile)
                        .build()
        );
    }

    @Operation(
            summary = "Get candidate profile by user ID",
            description = "Returns the candidate profile associated with a specific user. Accessible to recruiters and administrators."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Candidate profile retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Candidate profile not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> getProfileByUserId(
            @Parameter(description = "User ID of the candidate", example = "1")
            @PathVariable Long userId) {

        CandidateProfileResponse profile =
                candidateProfileService.getProfileByUserId(userId);

        return ResponseEntity.ok(
                ApiResponse.<CandidateProfileResponse>builder()
                        .success(true)
                        .message("Candidate profile fetched successfully.")
                        .data(profile)
                        .build()
        );
    }

    @Operation(
            summary = "Get all candidate profiles",
            description = "Returns a paginated list of all candidate profiles. Accessible to recruiters and administrators."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Candidate profiles retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<PageResponse<CandidateProfileResponse>>> getAllCandidates(
            @Parameter(description = "Pagination and sorting information")
            @PageableDefault(size = 10, sort = "id")
            Pageable pageable) {

        PageResponse<CandidateProfileResponse> candidates =
                candidateProfileService.getAllCandidates(pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<CandidateProfileResponse>>builder()
                        .success(true)
                        .message("Candidates fetched successfully.")
                        .data(candidates)
                        .build()
        );
    }
}