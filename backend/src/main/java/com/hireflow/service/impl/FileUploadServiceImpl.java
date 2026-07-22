package com.hireflow.service.impl;

import com.hireflow.dto.response.CandidateProfileResponse;
import com.hireflow.dto.response.CompanyResponse;
import com.hireflow.dto.response.RecruiterProfileResponse;
import com.hireflow.entity.CandidateProfile;
import com.hireflow.entity.Company;
import com.hireflow.entity.RecruiterProfile;
import com.hireflow.entity.User;
import com.hireflow.entity.enums.Role;
import com.hireflow.entity.enums.UploadCategory;
import com.hireflow.exception.ResourceNotFoundException;
import com.hireflow.exception.UnauthorizedException;
import com.hireflow.mapper.CandidateProfileMapper;
import com.hireflow.mapper.CompanyMapper;
import com.hireflow.mapper.RecruiterProfileMapper;
import com.hireflow.repository.CandidateProfileRepository;
import com.hireflow.repository.CompanyRepository;
import com.hireflow.repository.RecruiterProfileRepository;
import com.hireflow.repository.UserRepository;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.FileStorageService;
import com.hireflow.service.FileUploadService;
import com.hireflow.service.StoredFileInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final FileStorageService fileStorageService;

    private final CandidateProfileRepository candidateProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    private final CandidateProfileMapper candidateProfileMapper;
    private final RecruiterProfileMapper recruiterProfileMapper;
    private final CompanyMapper companyMapper;

    @Override
    @Transactional
    public CandidateProfileResponse uploadResume(MultipartFile file, Long candidateUserId) {

        CandidateProfile profile = getOrCreateCandidateProfile(candidateUserId);
        String previousPath = toRelativePath(profile.getResumeUrl());

        StoredFileInfo stored = fileStorageService.store(file, UploadCategory.RESUME);
        profile.setResumeUrl(stored.publicUrl());
        profile = candidateProfileRepository.save(profile);

        deleteQuietly(previousPath);

        return candidateProfileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public CandidateProfileResponse deleteResume(Long candidateUserId) {

        CandidateProfile profile = candidateProfileRepository.findByUserId(candidateUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found."));

        deleteQuietly(toRelativePath(profile.getResumeUrl()));
        profile.setResumeUrl(null);
        profile = candidateProfileRepository.save(profile);

        return candidateProfileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public CandidateProfileResponse uploadCandidateProfilePicture(MultipartFile file, Long candidateUserId) {

        CandidateProfile profile = getOrCreateCandidateProfile(candidateUserId);
        String previousPath = toRelativePath(profile.getProfilePictureUrl());

        StoredFileInfo stored = fileStorageService.store(file, UploadCategory.PROFILE_PICTURE);
        profile.setProfilePictureUrl(stored.publicUrl());
        profile = candidateProfileRepository.save(profile);

        deleteQuietly(previousPath);

        return candidateProfileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public CandidateProfileResponse deleteCandidateProfilePicture(Long candidateUserId) {

        CandidateProfile profile = candidateProfileRepository.findByUserId(candidateUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found."));

        deleteQuietly(toRelativePath(profile.getProfilePictureUrl()));
        profile.setProfilePictureUrl(null);
        profile = candidateProfileRepository.save(profile);

        return candidateProfileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public RecruiterProfileResponse uploadRecruiterProfilePicture(MultipartFile file, Long recruiterUserId) {

        RecruiterProfile profile = getOrCreateRecruiterProfile(recruiterUserId);
        String previousPath = toRelativePath(profile.getProfilePictureUrl());

        StoredFileInfo stored = fileStorageService.store(file, UploadCategory.PROFILE_PICTURE);
        profile.setProfilePictureUrl(stored.publicUrl());
        profile = recruiterProfileRepository.save(profile);

        deleteQuietly(previousPath);

        return recruiterProfileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public RecruiterProfileResponse deleteRecruiterProfilePicture(Long recruiterUserId) {

        RecruiterProfile profile = recruiterProfileRepository.findByUserId(recruiterUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found."));

        deleteQuietly(toRelativePath(profile.getProfilePictureUrl()));
        profile.setProfilePictureUrl(null);
        profile = recruiterProfileRepository.save(profile);

        return recruiterProfileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public CompanyResponse uploadCompanyLogo(MultipartFile file, Long companyId, UserPrincipal principal) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found."));

        assertCanManageCompany(companyId, principal);

        String previousPath = toRelativePath(company.getLogoUrl());

        StoredFileInfo stored = fileStorageService.store(file, UploadCategory.LOGO);
        company.setLogoUrl(stored.publicUrl());
        company = companyRepository.save(company);

        deleteQuietly(previousPath);

        return companyMapper.toResponse(company);
    }

    @Override
    @Transactional
    public CompanyResponse deleteCompanyLogo(Long companyId, UserPrincipal principal) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found."));

        assertCanManageCompany(companyId, principal);

        deleteQuietly(toRelativePath(company.getLogoUrl()));
        company.setLogoUrl(null);
        company = companyRepository.save(company);

        return companyMapper.toResponse(company);
    }

    @Override
    public Resource serveFile(String category, String fileName) {

        UploadCategory uploadCategory = resolveCategory(category);
        return fileStorageService.loadAsResource(uploadCategory.getFolder() + "/" + fileName);
    }

    @Override
    public String resolveContentType(String fileName) {

        String extension = fileName != null && fileName.contains(".")
                ? fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase()
                : "";

        return switch (extension) {
            case "pdf" -> "application/pdf";
            case "doc" -> "application/msword";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "webp" -> "image/webp";
            default -> "application/octet-stream";
        };
    }

    private CandidateProfile getOrCreateCandidateProfile(Long userId) {

        return candidateProfileRepository.findByUserId(userId)
                .orElseGet(() -> {

                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found."));

                    return candidateProfileRepository.save(
                            CandidateProfile.builder().user(user).build());
                });
    }

    private RecruiterProfile getOrCreateRecruiterProfile(Long userId) {

        return recruiterProfileRepository.findByUserId(userId)
                .orElseGet(() -> {

                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found."));

                    return recruiterProfileRepository.save(
                            RecruiterProfile.builder().user(user).build());
                });
    }

    private void assertCanManageCompany(Long companyId, UserPrincipal principal) {

        User user = principal.getUser();

        if (user.getRole() == Role.ADMIN) {
            return;
        }

        RecruiterProfile profile = recruiterProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new UnauthorizedException(
                        "You are not authorized to manage this company's logo."));

        if (profile.getCompany() == null || !profile.getCompany().getId().equals(companyId)) {
            throw new UnauthorizedException("You are not authorized to manage this company's logo.");
        }
    }

    private UploadCategory resolveCategory(String category) {

        return Arrays.stream(UploadCategory.values())
                .filter(c -> c.getFolder().equalsIgnoreCase(category))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("File not found."));
    }

    /**
     * Recovers the storage-relative path (e.g. "resumes/abc123.pdf") from a
     * previously issued public URL (e.g. "/api/files/serve/resumes/abc123.pdf"),
     * so the old file can be cleaned up after a replace. Returns null for
     * anything that doesn't look like one of our own served URLs.
     */
    private String toRelativePath(String publicUrl) {

        if (publicUrl == null || publicUrl.isBlank()) {
            return null;
        }

        int marker = publicUrl.indexOf("/serve/");

        if (marker == -1) {
            return null;
        }

        return publicUrl.substring(marker + "/serve/".length());
    }

    private void deleteQuietly(String relativePath) {

        if (relativePath == null) {
            return;
        }

        try {
            fileStorageService.delete(relativePath);
        } catch (Exception ignored) {
            // Best-effort cleanup of the previous file. A failure here must
            // never roll back the already-persisted new file reference.
        }
    }
}