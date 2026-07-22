package com.hireflow.controller;

import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.CandidateProfileResponse;
import com.hireflow.dto.response.CompanyResponse;
import com.hireflow.dto.response.RecruiterProfileResponse;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "File Management",
        description = "Upload, delete and retrieve resumes, profile pictures and company logos."
)
@SecurityRequirement(name = "Bearer Authentication")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @Operation(
            summary = "Upload resume",
            description = "Uploads or replaces the authenticated candidate's resume."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Resume uploaded successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid file", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Only candidates can upload resumes", content = @Content)
    })
    @PostMapping(value = "/resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> uploadResume(
            @Parameter(description = "Resume file (PDF/DOC/DOCX)")
            @RequestParam("file") MultipartFile file,
            @Parameter(hidden = true)
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

    @Operation(summary = "Delete resume", description = "Deletes the authenticated candidate's resume.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Resume deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @DeleteMapping("/resume")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> deleteResume(
            @Parameter(hidden = true)
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

    @Operation(summary = "Upload candidate profile picture", description = "Uploads or replaces the authenticated candidate's profile picture.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile picture uploaded successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid image", content = @Content)
    })
    @PostMapping(value = "/candidate-profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> uploadCandidateProfilePicture(
            @Parameter(description = "Image file")
            @RequestParam("file") MultipartFile file,
            @Parameter(hidden = true)
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

    @Operation(summary = "Delete candidate profile picture", description = "Deletes the authenticated candidate's profile picture.")
    @DeleteMapping("/candidate-profile-picture")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> deleteCandidateProfilePicture(
            @Parameter(hidden = true)
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

    @Operation(summary = "Upload recruiter profile picture", description = "Uploads or replaces the authenticated recruiter's profile picture.")
    @PostMapping(value = "/recruiter-profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<RecruiterProfileResponse>> uploadRecruiterProfilePicture(
            @Parameter(description = "Image file")
            @RequestParam("file") MultipartFile file,
            @Parameter(hidden = true)
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

    @Operation(summary = "Delete recruiter profile picture", description = "Deletes the authenticated recruiter's profile picture.")
    @DeleteMapping("/recruiter-profile-picture")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<RecruiterProfileResponse>> deleteRecruiterProfilePicture(
            @Parameter(hidden = true)
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

    @Operation(summary = "Upload company logo", description = "Uploads or replaces a company logo. Accessible to administrators and authorized recruiters.")
    @PostMapping(value = "/companies/{companyId}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<CompanyResponse>> uploadCompanyLogo(
            @Parameter(description = "Company ID", example = "1")
            @PathVariable Long companyId,
            @Parameter(description = "Logo image")
            @RequestParam("file") MultipartFile file,
            @Parameter(hidden = true)
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

    @Operation(summary = "Delete company logo", description = "Deletes the logo of a company.")
    @DeleteMapping("/companies/{companyId}/logo")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<CompanyResponse>> deleteCompanyLogo(
            @Parameter(description = "Company ID", example = "1")
            @PathVariable Long companyId,
            @Parameter(hidden = true)
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

    @Operation(
            summary = "Serve uploaded file",
            description = "Returns an uploaded file inline (resume, profile picture or company logo) for authenticated users."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "File returned successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "File not found", content = @Content)
    })
    @GetMapping("/serve/{category}/{fileName}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> serveFile(
            @Parameter(description = "File category", example = "resumes")
            @PathVariable String category,
            @Parameter(description = "Stored file name", example = "resume.pdf")
            @PathVariable String fileName) {

        Resource resource = fileUploadService.serveFile(category, fileName);
        String contentType = fileUploadService.resolveContentType(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }
}