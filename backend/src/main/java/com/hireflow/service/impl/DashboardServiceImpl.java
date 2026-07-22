package com.hireflow.service.impl;

import com.hireflow.dto.response.AdminDashboardResponse;
import com.hireflow.dto.response.CandidateDashboardResponse;
import com.hireflow.dto.response.RecruiterDashboardResponse;
import com.hireflow.entity.enums.ApplicationStatus;
import com.hireflow.entity.enums.Role;
import com.hireflow.repository.ApplicationRepository;
import com.hireflow.repository.CompanyRepository;
import com.hireflow.repository.InterviewRepository;
import com.hireflow.repository.JobRepository;
import com.hireflow.repository.UserRepository;
import com.hireflow.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final InterviewRepository interviewRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Override
    public CandidateDashboardResponse getCandidateDashboard(Long candidateId) {

        return CandidateDashboardResponse.builder()
                .appliedJobs(applicationRepository.countByCandidateId(candidateId))
                .shortlisted(applicationRepository.countByCandidateIdAndStatus(
                        candidateId, ApplicationStatus.SHORTLISTED))
                .interviewsScheduled(interviewRepository.countByApplicationCandidateId(candidateId))
                .rejected(applicationRepository.countByCandidateIdAndStatus(
                        candidateId, ApplicationStatus.REJECTED))
                .hired(applicationRepository.countByCandidateIdAndStatus(
                        candidateId, ApplicationStatus.HIRED))
                .withdrawn(applicationRepository.countByCandidateIdAndStatus(
                        candidateId, ApplicationStatus.WITHDRAWN))
                .build();
    }

    @Override
    public RecruiterDashboardResponse getRecruiterDashboard(Long recruiterId) {

        return RecruiterDashboardResponse.builder()
                .jobsPosted(jobRepository.countByCreatedById(recruiterId))
                .activeJobs(jobRepository.countByCreatedByIdAndActiveTrue(recruiterId))
                .totalApplications(applicationRepository.countByJobCreatedById(recruiterId))
                .interviewsScheduled(interviewRepository.countByApplicationJobCreatedById(recruiterId))
                .hired(applicationRepository.countByJobCreatedByIdAndStatus(
                        recruiterId, ApplicationStatus.HIRED))
                .build();
    }

    @Override
    public AdminDashboardResponse getAdminDashboard() {

        return AdminDashboardResponse.builder()
                .totalUsers(userRepository.count())
                .totalCandidates(userRepository.countByRole(Role.CANDIDATE))
                .totalRecruiters(userRepository.countByRole(Role.RECRUITER))
                .totalCompanies(companyRepository.count())
                .totalJobs(jobRepository.count())
                .activeJobs(jobRepository.countByActiveTrue())
                .totalApplications(applicationRepository.count())
                .totalInterviews(interviewRepository.count())
                .build();
    }
}