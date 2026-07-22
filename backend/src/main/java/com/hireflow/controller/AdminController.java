package com.hireflow.controller;

import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.PlatformStatisticsResponse;
import com.hireflow.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final StatisticsService statisticsService;

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