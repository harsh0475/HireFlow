package com.hireflow.mapper;

import com.hireflow.dto.request.InterviewRequest;
import com.hireflow.dto.request.InterviewUpdateRequest;
import com.hireflow.dto.response.InterviewResponse;
import com.hireflow.entity.Interview;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    uses = {ApplicationMapper.class},
    builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface InterviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "application", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "feedback", ignore = true)
    @Mapping(target = "outcome", ignore = true)
    Interview toEntity(InterviewRequest request);

    @Mapping(target = "applicationId", source = "application.id")
    @Mapping(target = "job", source = "application.job", qualifiedByName = "toJobSummary")
    @Mapping(target = "candidate", source = "application.candidate", qualifiedByName = "toCandidateSummary")
    InterviewResponse toResponse(Interview interview);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "application", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "feedback", ignore = true)
    @Mapping(target = "outcome", ignore = true)
    void updateInterviewFromRequest(
            InterviewUpdateRequest request,
            @MappingTarget Interview interview
    );
}