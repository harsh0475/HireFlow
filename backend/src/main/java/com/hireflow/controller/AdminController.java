package com.hireflow.controller;

import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.PlatformStatisticsResponse;
import com.hireflow.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(
        name = "Administration",
        description = "Administrative endpoints for monitoring and managing the HireFlow platform."
)
public class AdminController {

    private final StatisticsService statisticsService;

    @Operation(
            summary = "Get platform statistics",
            description = "Returns an overview of platform metrics including users, companies, jobs, applications and other administrative dashboard statistics. Accessible only to administrators."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Platform statistics retrieved successfully"
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
}