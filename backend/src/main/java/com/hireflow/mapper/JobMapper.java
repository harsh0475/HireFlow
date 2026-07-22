package com.hireflow.mapper;

import com.hireflow.dto.request.JobRequest;
import com.hireflow.dto.response.JobResponse;
import com.hireflow.dto.response.RecruiterSummaryResponse;
import com.hireflow.entity.Job;
import com.hireflow.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface JobMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "active", ignore = true)
    Job toEntity(JobRequest request);

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toRecruiterSummary")
    JobResponse toResponse(Job job);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateJobFromRequest(JobRequest request, @MappingTarget Job job);

    @Named("toRecruiterSummary")
    default RecruiterSummaryResponse toRecruiterSummary(User user) {

        if (user == null) {
            return null;
        }

        return RecruiterSummaryResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }
}