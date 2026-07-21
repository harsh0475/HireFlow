package com.hireflow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company extends BaseEntity {

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(length = 255)
    private String website;

    @Column(length = 255)
    private String logoUrl;

    @Column(length = 100)
    private String industry;

    @Column(length = 100)
    private String companySize;

    @Column(length = 150)
    private String headquarters;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}