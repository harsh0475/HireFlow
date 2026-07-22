package com.hireflow.service;

import com.hireflow.dto.response.CandidateProfileResponse;
import com.hireflow.dto.response.CompanyResponse;
import com.hireflow.dto.response.RecruiterProfileResponse;
import com.hireflow.security.UserPrincipal;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    CandidateProfileResponse uploadResume(MultipartFile file, Long candidateUserId);

    CandidateProfileResponse deleteResume(Long candidateUserId);

    CandidateProfileResponse uploadCandidateProfilePicture(MultipartFile file, Long candidateUserId);

    CandidateProfileResponse deleteCandidateProfilePicture(Long candidateUserId);

    RecruiterProfileResponse uploadRecruiterProfilePicture(MultipartFile file, Long recruiterUserId);

    RecruiterProfileResponse deleteRecruiterProfilePicture(Long recruiterUserId);

    CompanyResponse uploadCompanyLogo(MultipartFile file, Long companyId, UserPrincipal principal);

    CompanyResponse deleteCompanyLogo(Long companyId, UserPrincipal principal);

    Resource serveFile(String category, String fileName);

    String resolveContentType(String fileName);
}