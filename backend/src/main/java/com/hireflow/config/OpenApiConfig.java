package com.hireflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI openAPI() {

        SecurityScheme securityScheme = new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(
                        new Info()
                                .title("HireFlow API")
                                .version("1.0.0")
                                .description("""
                                        HireFlow is a recruitment management platform
                                        built using Spring Boot.

                                        Features:
                                        • Authentication & JWT
                                        • Candidate Management
                                        • Recruiter Management
                                        • Company Management
                                        • Job Management
                                        • Applications
                                        • Interviews
                                        • Notifications
                                        • Dashboard Analytics
                                        • File Upload
                                        • Admin Module
                                        """)
                                .contact(
                                        new Contact()
                                                .name("Harshit Kumar Singh")
                                                .email("your-email@example.com")
                                )
                                .license(
                                        new License()
                                                .name("MIT License")
                                )
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        SECURITY_SCHEME_NAME,
                                        securityScheme
                                )
                )
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(SECURITY_SCHEME_NAME)
                );
    }
}