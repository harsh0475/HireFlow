package com.hireflow.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.mail")
public class MailProperties {

    /**
     * "From" email address used when sending system emails.
     */
    private String from;

    /**
     * Display name shown alongside the "from" address.
     */
    private String fromName;
}