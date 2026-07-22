package com.hireflow.service.impl;

import com.hireflow.dto.response.NotificationResponse;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.entity.Notification;
import com.hireflow.entity.User;
import com.hireflow.entity.enums.NotificationType;
import com.hireflow.exception.ResourceNotFoundException;
import com.hireflow.mapper.NotificationMapper;
import com.hireflow.repository.NotificationRepository;
import com.hireflow.repository.UserRepository;
import com.hireflow.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional
    public NotificationResponse createNotification(
            Long recipientId,
            String title,
            String message,
            NotificationType type,
            Long relatedEntityId,
            String relatedEntityType) {

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        Notification notification = Notification.builder()
                .recipient(recipient)
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .relatedEntityId(relatedEntityId)
                .relatedEntityType(relatedEntityType)
                .build();

        notification = notificationRepository.save(notification);

        return notificationMapper.toResponse(notification);
    }

    @Override
    public PageResponse<NotificationResponse> getMyNotifications(Long userId, Pageable pageable) {

        Page<NotificationResponse> page = notificationRepository.findByRecipientId(userId, pageable)
                .map(notificationMapper::toResponse);

        return PageResponse.from(page);
    }

    @Override
    public PageResponse<NotificationResponse> getMyUnreadNotifications(Long userId, Pageable pageable) {

        Page<NotificationResponse> page =
                notificationRepository.findByRecipientIdAndIsRead(userId, false, pageable)
                        .map(notificationMapper::toResponse);

        return PageResponse.from(page);
    }

    @Override
    public long getUnreadCount(Long userId) {

        return notificationRepository.countByRecipientIdAndIsReadFalse(userId);
    }

    @Override
    @Transactional
    public NotificationResponse markAsRead(Long notificationId, Long userId) {

        Notification notification = notificationRepository.findByIdAndRecipientId(notificationId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Notification not found."));

        if (!Boolean.TRUE.equals(notification.getIsRead())) {
            notification.setIsRead(true);
            notification = notificationRepository.save(notification);
        }

        return notificationMapper.toResponse(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {

        notificationRepository.markAllAsReadForRecipient(userId);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId, Long userId) {

        Notification notification = notificationRepository.findByIdAndRecipientId(notificationId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Notification not found."));

        notificationRepository.delete(notification);
    }
}