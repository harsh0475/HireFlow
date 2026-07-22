package com.hireflow.service;

import com.hireflow.dto.response.NotificationResponse;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.entity.enums.NotificationType;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    /**
     * Internal API used by other services (application, interview, admin, etc.)
     * to raise a notification for a given user. Not exposed directly via the
     * controller.
     */
    NotificationResponse createNotification(
            Long recipientId,
            String title,
            String message,
            NotificationType type,
            Long relatedEntityId,
            String relatedEntityType
    );

    PageResponse<NotificationResponse> getMyNotifications(Long userId, Pageable pageable);

    PageResponse<NotificationResponse> getMyUnreadNotifications(Long userId, Pageable pageable);

    long getUnreadCount(Long userId);

    NotificationResponse markAsRead(Long notificationId, Long userId);

    void markAllAsRead(Long userId);

    void deleteNotification(Long notificationId, Long userId);
}