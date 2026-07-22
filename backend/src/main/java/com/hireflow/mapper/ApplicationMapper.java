package com.hireflow.mapper;

import com.hireflow.dto.response.ApplicationResponse;
import com.hireflow.dto.response.CandidateSummaryResponse;
import com.hireflow.dto.response.JobSummaryResponse;
import com.hireflow.entity.Application;
import com.hireflow.entity.Job;
import com.hireflow.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface ApplicationMapper {

    @Mapping(target = "job", source = "job", qualifiedByName = "toJobSummary")
    @Mapping(target = "candidate", source = "candidate", qualifiedByName = "toCandidateSummary")
    ApplicationResponse toResponse(Application application);

    @Named("toJobSummary")
    default JobSummaryResponse toJobSummary(Job job) {

        if (job == null) {
            return null;
        }

        return JobSummaryResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .location(job.getLocation())
                .employmentType(job.getEmploymentType())
                .experienceLevel(job.getExperienceLevel())
                .company(toCompanyResponse(job))
                .build();
    }

    @Named("toCandidateSummary")
    default CandidateSummaryResponse toCandidateSummary(User user) {

        if (user == null) {
            return null;
        }

        return CandidateSummaryResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    default com.hireflow.dto.response.CompanyResponse toCompanyResponse(Job job) {

        if (job.getCompany() == null) {
            return null;
        }

        return com.hireflow.dto.response.CompanyResponse.builder()
                .id(job.getCompany().getId())
                .name(job.getCompany().getName())
                .logoUrl(job.getCompany().getLogoUrl())
                .industry(job.getCompany().getIndustry())
                .build();
    }
}