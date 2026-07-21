package com.hireflow.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CompanyResponse {

    private Long id;

    private String name;

    private String description;

    private String website;

    private String logoUrl;

    private String industry;

    private String companySize;

    private String headquarters;

    private Boolean active;
}