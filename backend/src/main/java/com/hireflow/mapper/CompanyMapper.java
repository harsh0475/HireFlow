package com.hireflow.mapper;

import com.hireflow.dto.request.CompanyRequest;
import com.hireflow.dto.response.CompanyResponse;
import com.hireflow.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    Company toEntity(CompanyRequest request);

    CompanyResponse toResponse(Company company);

    void updateCompanyFromRequest(
            CompanyRequest request,
            @MappingTarget Company company
    );
}