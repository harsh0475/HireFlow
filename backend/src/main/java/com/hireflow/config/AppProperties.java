package com.hireflow.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    /**
     * Base URL of the frontend application, used to build links inside emails
     * (e.g. the password reset link).
     */
    private String frontendBaseUrl;
}