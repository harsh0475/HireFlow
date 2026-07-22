package com.hireflow.repository;

import com.hireflow.entity.Application;
import com.hireflow.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {

    Optional<Application> findByJobIdAndCandidateId(Long jobId, Long candidateId);

    boolean existsByJobIdAndCandidateIdAndActiveTrue(Long jobId, Long candidateId);

    long countByCandidateId(Long candidateId);

    long countByCandidateIdAndStatus(Long candidateId, ApplicationStatus status);

    long countByJobCreatedById(Long recruiterId);

    long countByJobCreatedByIdAndStatus(Long recruiterId, ApplicationStatus status);
}