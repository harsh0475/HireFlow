package com.hireflow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "candidate_profiles")
public class CandidateProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 255)
    private String resumeUrl;

    @ElementCollection
    @CollectionTable(name = "candidate_skills", joinColumns = @JoinColumn(name = "candidate_profile_id"))
    @Column(name = "skill", length = 100)
    @Builder.Default
    private Set<String> skills = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String experience;

    @Column(columnDefinition = "TEXT")
    private String education;

    @Column(length = 255)
    private String linkedIn;

    @Column(length = 255)
    private String portfolio;

    @Column(length = 150)
    private String currentCompany;

    private Double currentCtc;

    private Double expectedCtc;

    @Column(length = 50)
    private String noticePeriod;

    @Column(length = 150)
    private String location;
}