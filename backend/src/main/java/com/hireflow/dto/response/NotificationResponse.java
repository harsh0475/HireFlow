package com.hireflow.dto.response;

import com.hireflow.entity.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;

    private String title;

    private String message;

    private NotificationType type;

    private Boolean isRead;

    private Long relatedEntityId;

    private String relatedEntityType;

    private LocalDateTime createdAt;
}