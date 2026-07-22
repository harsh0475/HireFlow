package com.hireflow.mapper;

import com.hireflow.dto.request.CandidateProfileRequest;
import com.hireflow.dto.response.CandidateProfileResponse;
import com.hireflow.entity.CandidateProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    componentModel = "spring",
    builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface CandidateProfileMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    CandidateProfile toEntity(CandidateProfileRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    CandidateProfileResponse toResponse(CandidateProfile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateProfileFromRequest(CandidateProfileRequest request, @MappingTarget CandidateProfile profile);
}