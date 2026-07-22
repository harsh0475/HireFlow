package com.hireflow.repository;

import com.hireflow.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long>, JpaSpecificationExecutor<Interview> {

    List<Interview> findByApplicationId(Long applicationId);

    long countByApplicationCandidateId(Long candidateId);

    long countByApplicationJobCreatedById(Long recruiterId);
}