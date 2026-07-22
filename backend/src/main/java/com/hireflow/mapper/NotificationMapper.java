package com.hireflow.mapper;

import com.hireflow.dto.response.NotificationResponse;
import com.hireflow.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponse toResponse(Notification notification);
}