package com.hireflow.controller;

import com.hireflow.dto.request.RecruiterProfileRequest;
import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.dto.response.RecruiterProfileResponse;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.RecruiterProfileService;
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
@RequestMapping("/api/recruiter-profiles")
@RequiredArgsConstructor
@Tag(
        name = "Recruiter Profiles",
        description = "Endpoints for recruiters to manage their profiles and for authenticated users to view recruiter profiles."
)
@SecurityRequirement(name = "Bearer Authentication")
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;

    @Operation(
            summary = "Create or update my recruiter profile",
            description = "Creates a new recruiter profile or updates the existing profile for the authenticated recruiter or administrator."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Recruiter profile saved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<RecruiterProfileResponse>> createOrUpdateMyProfile(
            @Valid @RequestBody RecruiterProfileRequest request,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        RecruiterProfileResponse profile =
                recruiterProfileService.createOrUpdateProfile(
                        request,
                        principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<RecruiterProfileResponse>builder()
                        .success(true)
                        .message("Recruiter profile saved successfully.")
                        .data(profile)
                        .build()
        );
    }

    @Operation(
            summary = "Get my recruiter profile",
            description = "Returns the recruiter profile of the authenticated recruiter or administrator."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Recruiter profile retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Recruiter profile not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<RecruiterProfileResponse>> getMyProfile(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        RecruiterProfileResponse profile =
                recruiterProfileService.getMyProfile(principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<RecruiterProfileResponse>builder()
                        .success(true)
                        .message("Recruiter profile fetched successfully.")
                        .data(profile)
                        .build()
        );
    }

    @Operation(
            summary = "Get recruiter profile by user ID",
            description = "Returns the recruiter profile associated with a specific user."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Recruiter profile retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Recruiter profile not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<RecruiterProfileResponse>> getProfileByUserId(
            @Parameter(description = "User ID of the recruiter", example = "1")
            @PathVariable Long userId) {

        RecruiterProfileResponse profile =
                recruiterProfileService.getProfileByUserId(userId);

        return ResponseEntity.ok(
                ApiResponse.<RecruiterProfileResponse>builder()
                        .success(true)
                        .message("Recruiter profile fetched successfully.")
                        .data(profile)
                        .build()
        );
    }

    @Operation(
            summary = "Get all recruiter profiles",
            description = "Returns a paginated list of recruiter profiles available to authenticated users."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Recruiter profiles retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PageResponse<RecruiterProfileResponse>>> getAllRecruiters(
            @Parameter(description = "Pagination and sorting information")
            @PageableDefault(size = 10, sort = "id")
            Pageable pageable) {

        PageResponse<RecruiterProfileResponse> recruiters =
                recruiterProfileService.getAllRecruiters(pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<RecruiterProfileResponse>>builder()
                        .success(true)
                        .message("Recruiters fetched successfully.")
                        .data(recruiters)
                        .build()
        );
    }
}