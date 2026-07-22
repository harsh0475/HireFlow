package com.hireflow.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI hireFlowOpenAPI() {

        SecurityScheme securityScheme = new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Enter the JWT access token. Example: Bearer eyJhbGciOiJIUzI1NiJ9...");

        return new OpenAPI()

                .info(new Info()
                        .title("HireFlow API")
                        .version("1.0.0")
                        .description("""
                                HireFlow is a modern Recruitment Management System built using Spring Boot.

                                The API enables secure management of the complete recruitment lifecycle including:

                                • JWT Authentication & Authorization
                                • User Management
                                • Candidate Profiles
                                • Recruiter Profiles
                                • Company Management
                                • Job Posting & Search
                                • Job Applications
                                • Interview Scheduling
                                • Notification Management
                                • Dashboard Analytics
                                • File Upload Management
                                • Administrative Operations

                                Authentication

                                Most endpoints require JWT authentication.

                                1. Login using /api/auth/login
                                2. Copy the returned access token.
                                3. Click the Authorize button in Swagger UI.
                                4. Enter:

                                   Bearer <your_access_token>

                                """)
                        .termsOfService("https://example.com/terms")
                        .contact(new Contact()
                                .name("Harshit Kumar Singh")
                                .email("harshksingh2004@gmail.com")
                                .url("https://github.com/harsh0475"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))

                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081")
                                .description("Local Development Server"),

                        new Server()
                                .url("https://api.example.com")
                                .description("Production Server")
                ))

                .components(new Components()
                        .addSecuritySchemes(
                                SECURITY_SCHEME_NAME,
                                securityScheme))

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(SECURITY_SCHEME_NAME))

                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                        .url("https://github.com/harsh0475/HireFlow.git"));
    }
}