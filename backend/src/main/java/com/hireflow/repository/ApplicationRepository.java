package com.hireflow.repository;

import com.hireflow.dto.response.JobApplicationCountResponse;
import com.hireflow.entity.Application;
import com.hireflow.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {

    Optional<Application> findByJobIdAndCandidateId(Long jobId, Long candidateId);

    boolean existsByJobIdAndCandidateIdAndActiveTrue(Long jobId, Long candidateId);

    long countByCandidateId(Long candidateId);

    long countByCandidateIdAndStatus(Long candidateId, ApplicationStatus status);

    long countByJobCreatedById(Long recruiterId);

    long countByJobCreatedByIdAndStatus(Long recruiterId, ApplicationStatus status);

    @Query("SELECT new com.hireflow.dto.response.JobApplicationCountResponse(j.id, j.title, COUNT(a)) " +
            "FROM Job j LEFT JOIN Application a ON a.job = j " +
            "GROUP BY j.id, j.title " +
            "ORDER BY j.title ASC")
    List<JobApplicationCountResponse> countApplicationsPerJob();
}