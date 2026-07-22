package com.hireflow.controller;

import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.NotificationResponse;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.NotificationService;
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
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getMyNotifications(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

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

    @GetMapping("/unread")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getMyUnreadNotifications(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

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

    @GetMapping("/unread-count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
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

    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(
            @PathVariable Long id,
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

    @PutMapping("/read-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Object>> markAllAsRead(
            @AuthenticationPrincipal UserPrincipal principal) {

        notificationService.markAllAsRead(principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("All notifications marked as read.")
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Object>> deleteNotification(
            @PathVariable Long id,
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