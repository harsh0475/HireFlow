package com.hireflow.mapper;

import com.hireflow.dto.response.UserResponse;
import com.hireflow.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
}