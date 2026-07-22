package com.hireflow.service.impl;

import com.hireflow.dto.request.RecruiterProfileRequest;
import com.hireflow.dto.response.PageResponse;
import com.hireflow.dto.response.RecruiterProfileResponse;
import com.hireflow.entity.Company;
import com.hireflow.entity.RecruiterProfile;
import com.hireflow.entity.User;
import com.hireflow.exception.ResourceNotFoundException;
import com.hireflow.mapper.RecruiterProfileMapper;
import com.hireflow.repository.CompanyRepository;
import com.hireflow.repository.RecruiterProfileRepository;
import com.hireflow.repository.UserRepository;
import com.hireflow.service.RecruiterProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruiterProfileServiceImpl implements RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RecruiterProfileMapper recruiterProfileMapper;

    @Override
    @Transactional
    public RecruiterProfileResponse createOrUpdateProfile(RecruiterProfileRequest request, Long userId) {

        RecruiterProfile profile = recruiterProfileRepository.findByUserId(userId)
                .orElse(null);

        Company company = null;

        if (request.getCompanyId() != null) {
            company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Company not found."));
        }

        if (profile == null) {

            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("User not found."));

            profile = RecruiterProfile.builder()
                    .user(user)
                    .company(company)
                    .designation(request.getDesignation())
                    .phone(request.getPhone())
                    .linkedIn(request.getLinkedIn())
                    .build();

        } else {

            profile.setCompany(company);
            profile.setDesignation(request.getDesignation());
            profile.setPhone(request.getPhone());
            profile.setLinkedIn(request.getLinkedIn());
        }

        profile = recruiterProfileRepository.save(profile);

        return recruiterProfileMapper.toResponse(profile);
    }

    @Override
    public RecruiterProfileResponse getMyProfile(Long userId) {

        RecruiterProfile profile = recruiterProfileRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Recruiter profile not found."));

        return recruiterProfileMapper.toResponse(profile);
    }

    @Override
    public RecruiterProfileResponse getProfileByUserId(Long userId) {

        RecruiterProfile profile = recruiterProfileRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Recruiter profile not found."));

        return recruiterProfileMapper.toResponse(profile);
    }

    @Override
    public PageResponse<RecruiterProfileResponse> getAllRecruiters(Pageable pageable) {

        Page<RecruiterProfileResponse> page = recruiterProfileRepository.findAll(pageable)
                .map(recruiterProfileMapper::toResponse);

        return PageResponse.from(page);
    }
}