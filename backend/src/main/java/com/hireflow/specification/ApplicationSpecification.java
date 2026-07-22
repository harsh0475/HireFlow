package com.hireflow.specification;

import com.hireflow.entity.Application;
import com.hireflow.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.domain.Specification;

public class ApplicationSpecification {

    private ApplicationSpecification() {
    }

    public static Specification<Application> hasCandidate(Long candidateId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("candidate").get("id"), candidateId);
    }

    public static Specification<Application> hasJob(Long jobId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("job").get("id"), jobId);
    }

    public static Specification<Application> hasRecruiter(Long recruiterId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("job").get("createdBy").get("id"), recruiterId);
    }

    public static Specification<Application> hasCompany(Long companyId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("job").get("company").get("id"), companyId);
    }

    public static Specification<Application> hasStatus(ApplicationStatus status) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Application> isActive() {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("active"));
    }
}