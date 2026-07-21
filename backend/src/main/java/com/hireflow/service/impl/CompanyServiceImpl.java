package com.hireflow.service.impl;

import com.hireflow.dto.request.CompanyRequest;
import com.hireflow.dto.response.CompanyResponse;
import com.hireflow.entity.Company;
import com.hireflow.exception.BadRequestException;
import com.hireflow.exception.ResourceNotFoundException;
import com.hireflow.mapper.CompanyMapper;
import com.hireflow.repository.CompanyRepository;
import com.hireflow.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    public CompanyResponse createCompany(CompanyRequest request) {

        if (companyRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("Company already exists.");
        }

        Company company = companyMapper.toEntity(request);
        company.setActive(true);

        company = companyRepository.save(company);

        return companyMapper.toResponse(company);
    }

    @Override
    public CompanyResponse updateCompany(Long id, CompanyRequest request) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Company not found."));

        if (!company.getName().equalsIgnoreCase(request.getName())
                && companyRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("Company name already exists.");
        }

        companyMapper.updateCompanyFromRequest(request, company);

        company = companyRepository.save(company);

        return companyMapper.toResponse(company);
    }

    @Override
    public CompanyResponse getCompanyById(Long id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Company not found."));

        return companyMapper.toResponse(company);
    }

    @Override
    public List<CompanyResponse> getAllCompanies() {

        return companyRepository.findAll()
                .stream()
                .map(companyMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteCompany(Long id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Company not found."));

        companyRepository.delete(company);
    }
}