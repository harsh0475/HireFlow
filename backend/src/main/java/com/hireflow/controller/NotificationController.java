package com.hireflow.controller;

import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.NotificationResponse;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(
        name = "Notification Management",
        description = "Endpoints for retrieving, managing and marking notifications as read."
)
@SecurityRequirement(name = "Bearer Authentication")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(
            summary = "Get my notifications",
            description = "Returns a paginated list of notifications for the authenticated user."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getMyNotifications(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal,
            @Parameter(description = "Pagination and sorting information")
            @PageableDefault(size = 10, sort = "createdAt")
            Pageable pageable) {

        PageResponse<NotificationResponse> notifications =
                notificationService.getMyNotifications(principal.getUser().getId(), pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<NotificationResponse>>builder()
                        .success(true)
                        .message("Notifications fetched successfully.")
                        .data(notifications)
                        .build()
        );
    }

    @Operation(
            summary = "Get unread notifications",
            description = "Returns only unread notifications for the authenticated user."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Unread notifications retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/unread")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getMyUnreadNotifications(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal,
            @Parameter(description = "Pagination and sorting information")
            @PageableDefault(size = 10, sort = "createdAt")
            Pageable pageable) {

        PageResponse<NotificationResponse> notifications =
                notificationService.getMyUnreadNotifications(principal.getUser().getId(), pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<NotificationResponse>>builder()
                        .success(true)
                        .message("Unread notifications fetched successfully.")
                        .data(notifications)
                        .build()
        );
    }

    @Operation(
            summary = "Get unread notification count",
            description = "Returns the total number of unread notifications for the authenticated user."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Unread notification count retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/unread-count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        long count = notificationService.getUnreadCount(principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<Long>builder()
                        .success(true)
                        .message("Unread notification count fetched successfully.")
                        .data(count)
                        .build()
        );
    }

    @Operation(
            summary = "Mark notification as read",
            description = "Marks a specific notification as read."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification marked as read"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(
            @Parameter(description = "Notification ID", example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        NotificationResponse notification =
                notificationService.markAsRead(id, principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.<NotificationResponse>builder()
                        .success(true)
                        .message("Notification marked as read.")
                        .data(notification)
                        .build()
        );
    }

    @Operation(
            summary = "Mark all notifications as read",
            description = "Marks every notification belonging to the authenticated user as read."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "All notifications marked as read"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PutMapping("/read-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Object>> markAllAsRead(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        notificationService.markAllAsRead(principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("All notifications marked as read.")
                        .build()
        );
    }

    @Operation(
            summary = "Delete notification",
            description = "Deletes a notification belonging to the authenticated user."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Object>> deleteNotification(
            @Parameter(description = "Notification ID", example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserPrincipal principal) {

        notificationService.deleteNotification(id, principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Notification deleted successfully.")
                        .build()
        );
    }
}