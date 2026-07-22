package com.hireflow.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * Secret key used to sign JWT tokens.
     */
    private String secret;

    /**
     * Access token expiration time in milliseconds.
     */
    private long expiration;

    /**
     * Refresh token expiration time in milliseconds.
     */
    private long refreshExpiration;

    /**
     * Password reset token expiration time in milliseconds.
     */
    private long passwordResetExpiration;
}