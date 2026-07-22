package com.hireflow.specification;

import com.hireflow.entity.Interview;
import org.springframework.data.jpa.domain.Specification;

public class InterviewSpecification {

    private InterviewSpecification() {
    }

    public static Specification<Interview> hasCandidate(Long candidateId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("application").get("candidate").get("id"), candidateId);
    }

    public static Specification<Interview> hasRecruiter(Long recruiterId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("application").get("job").get("createdBy").get("id"), recruiterId);
    }

    public static Specification<Interview> hasApplication(Long applicationId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("application").get("id"), applicationId);
    }
}