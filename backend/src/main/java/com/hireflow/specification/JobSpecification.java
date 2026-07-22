package com.hireflow.specification;

import com.hireflow.dto.request.JobFilterRequest;
import com.hireflow.entity.Job;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class JobSpecification {

    private JobSpecification() {
    }

    public static Specification<Job> build(JobFilterRequest filter) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.isTrue(root.get("active")));

            if (filter == null) {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }

            if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
                String pattern = "%" + filter.getKeyword().toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern)
                ));
            }

            if (filter.getLocation() != null && !filter.getLocation().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("location")),
                        "%" + filter.getLocation().toLowerCase() + "%"
                ));
            }

            if (filter.getEmploymentType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("employmentType"), filter.getEmploymentType()));
            }

            if (filter.getExperienceLevel() != null) {
                predicates.add(criteriaBuilder.equal(root.get("experienceLevel"), filter.getExperienceLevel()));
            }

            if (filter.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
            }

            if (filter.getCompanyId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), filter.getCompanyId()));
            }

            if (filter.getRemote() != null) {
                predicates.add(criteriaBuilder.equal(root.get("remote"), filter.getRemote()));
            }

            if (filter.getMinSalary() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("maxSalary"), filter.getMinSalary()));
            }

            if (filter.getMaxSalary() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("minSalary"), filter.getMaxSalary()));
            }

            if (filter.getSkill() != null && !filter.getSkill().isBlank()) {
                predicates.add(criteriaBuilder.isMember(filter.getSkill(), root.get("skills")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Job> hasCompany(Long companyId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("company").get("id"), companyId);
    }

    public static Specification<Job> hasCreatedBy(Long userId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("createdBy").get("id"), userId);
    }

    public static Specification<Job> isActive() {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("active"));
    }
}