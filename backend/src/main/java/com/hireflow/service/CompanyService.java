package com.hireflow.service;

import com.hireflow.dto.request.CompanyRequest;
import com.hireflow.dto.response.CompanyResponse;

import java.util.List;

public interface CompanyService {

    CompanyResponse createCompany(CompanyRequest request);

    CompanyResponse updateCompany(Long id, CompanyRequest request);

    CompanyResponse getCompanyById(Long id);

    List<CompanyResponse> getAllCompanies();

    void deleteCompany(Long id);
}