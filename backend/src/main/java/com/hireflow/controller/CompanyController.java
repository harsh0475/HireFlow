package com.hireflow.controller;

import com.hireflow.dto.request.CompanyRequest;
import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.CompanyResponse;
import com.hireflow.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(
        name = "Company Management",
        description = "Endpoints for creating, updating, viewing and deleting companies."
)
@SecurityRequirement(name = "Bearer Authentication")
public class CompanyController {

    private final CompanyService companyService;

    @Operation(
            summary = "Create company",
            description = "Creates a new company. Accessible to ADMIN and RECRUITER roles."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Company created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<CompanyResponse>> createCompany(
            @Valid @RequestBody CompanyRequest request) {

        CompanyResponse company = companyService.createCompany(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<CompanyResponse>builder()
                        .success(true)
                        .message("Company created successfully.")
                        .data(company)
                        .build());
    }

    @Operation(
            summary = "Update company",
            description = "Updates the details of an existing company. Accessible to ADMIN and RECRUITER roles."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Company updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Company not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<ApiResponse<CompanyResponse>> updateCompany(
            @Parameter(description = "Unique company ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CompanyRequest request) {

        CompanyResponse company = companyService.updateCompany(id, request);

        return ResponseEntity.ok(
                ApiResponse.<CompanyResponse>builder()
                        .success(true)
                        .message("Company updated successfully.")
                        .data(company)
                        .build()
        );
    }

    @Operation(
            summary = "Get company by ID",
            description = "Retrieves details of a specific company."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Company retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Company not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CompanyResponse>> getCompany(
            @Parameter(description = "Unique company ID", example = "1")
            @PathVariable Long id) {

        CompanyResponse company = companyService.getCompanyById(id);

        return ResponseEntity.ok(
                ApiResponse.<CompanyResponse>builder()
                        .success(true)
                        .message("Company fetched successfully.")
                        .data(company)
                        .build()
        );
    }

    @Operation(
            summary = "Get all companies",
            description = "Returns a list of all registered companies."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Companies retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> getAllCompanies() {

        List<CompanyResponse> companies = companyService.getAllCompanies();

        return ResponseEntity.ok(
                ApiResponse.<List<CompanyResponse>>builder()
                        .success(true)
                        .message("Companies fetched successfully.")
                        .data(companies)
                        .build()
        );
    }

    @Operation(
            summary = "Delete company",
            description = "Permanently deletes a company. Accessible only to ADMIN users."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Company deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Company not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(
            @Parameter(description = "Unique company ID", example = "1")
            @PathVariable Long id) {

        companyService.deleteCompany(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Company deleted successfully.")
                        .build()
        );
    }
}