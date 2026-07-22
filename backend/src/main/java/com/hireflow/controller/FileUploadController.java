package com.hireflow.controller;

import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.CandidateProfileResponse;
import com.hireflow.dto.response.CompanyResponse;
import com.hireflow.dto.response.RecruiterProfileResponse;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    // ---------------------------------------------------------------
    // Resume (candidate)
    // ---------------------------------------------------------------

    @PostMapping(value = "/resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> uploadResume(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal principal) {

        CandidateProfileResponse profile =
                fileUploadService.uploadResume(file, principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<CandidateProfileResponse>builder()
                        .success(true)
                        .message("Resume uploaded successfully.")
                        .data(profile)
                        .build()
        );
    }

    @DeleteMapping("/resume")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> deleteResume(
            @AuthenticationPrincipal UserPrincipal principal) {

        CandidateProfileResponse profile =
                fileUploadService.deleteResume(principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<CandidateProfileResponse>builder()
                        .success(true)
                        .message("Resume deleted successfully.")
                        .data(profile)
                        .build()
        );
    }

    // ---------------------------------------------------------------
    // Profile picture (candidate)
    // ---------------------------------------------------------------

    @PostMapping(value = "/candidate-profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> uploadCandidateProfilePicture(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal principal) {

        CandidateProfileResponse profile =
                fileUploadService.uploadCandidateProfilePicture(file, principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<CandidateProfileResponse>builder()
                        .success(true)
                        .message("Profile picture uploaded successfully.")
                        .data(profile)
                        .build()
        );
    }

    @DeleteMapping("/candidate-profile-picture")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> deleteCandidateProfilePicture(
            @AuthenticationPrincipal UserPrincipal principal) {

        CandidateProfileResponse profile =
                fileUploadService.deleteCandidateProfilePicture(principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<CandidateProfileResponse>builder()
                        .success(true)
                        .message("Profile picture deleted successfully.")
                        .data(profile)
                        .build()
        );
    }

    // ---------------------------------------------------------------
    // Profile picture (recruiter)
    // ---------------------------------------------------------------

    @PostMapping(value = "/recruiter-profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<RecruiterProfileResponse>> uploadRecruiterProfilePicture(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal principal) {

        RecruiterProfileResponse profile =
                fileUploadService.uploadRecruiterProfilePicture(file, principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<RecruiterProfileResponse>builder()
                        .success(true)
                        .message("Profile picture uploaded successfully.")
                        .data(profile)
                        .build()
        );
    }

    @DeleteMapping("/recruiter-profile-picture")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<RecruiterProfileResponse>> deleteRecruiterProfilePicture(
            @AuthenticationPrincipal UserPrincipal principal) {

        RecruiterProfileResponse profile =
                fileUploadService.deleteRecruiterProfilePicture(principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<RecruiterProfileResponse>builder()
                        .success(true)
                        .message("Profile picture deleted successfully.")
                        .data(profile)
                        .build()
        );
    }

    // ---------------------------------------------------------------
    // Company logo (recruiter belonging to the company, or admin)
    // ---------------------------------------------------------------

    @PostMapping(value = "/companies/{companyId}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<CompanyResponse>> uploadCompanyLogo(
            @PathVariable Long companyId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal principal) {

        CompanyResponse company =
                fileUploadService.uploadCompanyLogo(file, companyId, principal);

        return ResponseEntity.ok(
                ApiResponse.<CompanyResponse>builder()
                        .success(true)
                        .message("Company logo uploaded successfully.")
                        .data(company)
                        .build()
        );
    }

    @DeleteMapping("/companies/{companyId}/logo")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<CompanyResponse>> deleteCompanyLogo(
            @PathVariable Long companyId,
            @AuthenticationPrincipal UserPrincipal principal) {

        CompanyResponse company =
                fileUploadService.deleteCompanyLogo(companyId, principal);

        return ResponseEntity.ok(
                ApiResponse.<CompanyResponse>builder()
                        .success(true)
                        .message("Company logo deleted successfully.")
                        .data(company)
                        .build()
        );
    }

    // ---------------------------------------------------------------
    // Serve (any authenticated user)
    // ---------------------------------------------------------------

    @GetMapping("/serve/{category}/{fileName}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> serveFile(
            @PathVariable String category,
            @PathVariable String fileName) {

        Resource resource = fileUploadService.serveFile(category, fileName);
        String contentType = fileUploadService.resolveContentType(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }
}