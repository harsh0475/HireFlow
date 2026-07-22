package com.hireflow.repository;

import com.hireflow.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    long countByCreatedById(Long recruiterId);

    long countByCreatedByIdAndActiveTrue(Long recruiterId);

    long countByActiveTrue();
}