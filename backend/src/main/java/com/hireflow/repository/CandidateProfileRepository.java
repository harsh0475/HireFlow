package com.hireflow.repository;

import com.hireflow.entity.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long>, JpaSpecificationExecutor<CandidateProfile> {

    Optional<CandidateProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}