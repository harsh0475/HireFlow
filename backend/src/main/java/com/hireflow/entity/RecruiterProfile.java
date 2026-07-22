package com.hireflow.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recruiter_profiles")
public class RecruiterProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(length = 150)
    private String designation;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String linkedIn;
}