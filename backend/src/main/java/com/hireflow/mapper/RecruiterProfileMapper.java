package com.hireflow.mapper;

import com.hireflow.dto.response.RecruiterProfileResponse;
import com.hireflow.entity.RecruiterProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface RecruiterProfileMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    RecruiterProfileResponse toResponse(RecruiterProfile profile);
}