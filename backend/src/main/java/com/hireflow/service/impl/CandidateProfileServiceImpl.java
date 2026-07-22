package com.hireflow.service.impl;

import com.hireflow.dto.request.CandidateProfileRequest;
import com.hireflow.dto.response.CandidateProfileResponse;
import com.hireflow.entity.CandidateProfile;
import com.hireflow.entity.User;
import com.hireflow.exception.ResourceNotFoundException;
import com.hireflow.mapper.CandidateProfileMapper;
import com.hireflow.repository.CandidateProfileRepository;
import com.hireflow.repository.UserRepository;
import com.hireflow.service.CandidateProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CandidateProfileServiceImpl implements CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final UserRepository userRepository;
    private final CandidateProfileMapper candidateProfileMapper;

    @Override
    @Transactional
    public CandidateProfileResponse createOrUpdateProfile(CandidateProfileRequest request, Long userId) {

        CandidateProfile profile = candidateProfileRepository.findByUserId(userId)
                .orElse(null);

        if (profile == null) {

            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("User not found."));

            profile = candidateProfileMapper.toEntity(request);
            profile.setUser(user);

        } else {
            candidateProfileMapper.updateProfileFromRequest(request, profile);
        }

        profile = candidateProfileRepository.save(profile);

        return candidateProfileMapper.toResponse(profile);
    }

    @Override
    public CandidateProfileResponse getMyProfile(Long userId) {

        CandidateProfile profile = candidateProfileRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Candidate profile not found."));

        return candidateProfileMapper.toResponse(profile);
    }

    @Override
    public CandidateProfileResponse getProfileByUserId(Long userId) {

        CandidateProfile profile = candidateProfileRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Candidate profile not found."));

        return candidateProfileMapper.toResponse(profile);
    }
}