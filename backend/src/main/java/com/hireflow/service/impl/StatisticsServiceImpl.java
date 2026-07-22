package com.hireflow.service.impl;

import com.hireflow.dto.response.MonthlyRegistrationResponse;
import com.hireflow.dto.response.PlatformStatisticsResponse;
import com.hireflow.entity.enums.Role;
import com.hireflow.repository.ApplicationRepository;
import com.hireflow.repository.CompanyRepository;
import com.hireflow.repository.InterviewRepository;
import com.hireflow.repository.JobRepository;
import com.hireflow.repository.UserRepository;
import com.hireflow.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;
    private final InterviewRepository interviewRepository;

    @Override
    public PlatformStatisticsResponse getPlatformStatistics() {

        List<MonthlyRegistrationResponse> monthlyRegistrations =
                userRepository.countMonthlyRegistrations().stream()
                        .map(row -> new MonthlyRegistrationResponse(
                                (String) row[0],
                                ((Number) row[1]).longValue()
                        ))
                        .toList();

        return PlatformStatisticsResponse.builder()
                .totalUsers(userRepository.count())
                .totalRecruiters(userRepository.countByRole(Role.RECRUITER))
                .totalCandidates(userRepository.countByRole(Role.CANDIDATE))
                .totalCompanies(companyRepository.count())
                .totalJobs(jobRepository.count())
                .activeJobs(jobRepository.countByActiveTrue())
                .totalApplications(applicationRepository.count())
                .totalInterviews(interviewRepository.count())
                .jobsPerCompany(jobRepository.countJobsPerCompany())
                .applicationsPerJob(applicationRepository.countApplicationsPerJob())
                .monthlyRegistrations(monthlyRegistrations)
                .build();
    }
}