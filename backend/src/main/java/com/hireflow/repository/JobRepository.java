package com.hireflow.repository;

import com.hireflow.dto.response.CompanyJobCountResponse;
import com.hireflow.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    long countByCreatedById(Long recruiterId);

    long countByCreatedByIdAndActiveTrue(Long recruiterId);

    long countByActiveTrue();

    @Query("SELECT new com.hireflow.dto.response.CompanyJobCountResponse(c.id, c.name, COUNT(j)) " +
            "FROM Company c LEFT JOIN Job j ON j.company = c " +
            "GROUP BY c.id, c.name " +
            "ORDER BY c.name ASC")
    List<CompanyJobCountResponse> countJobsPerCompany();
}