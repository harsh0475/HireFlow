package com.hireflow.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the File Upload Module (Phase 3).
 * Bound from the {@code app.file.*} keys in application.yaml.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.file")
public class FileStorageProperties {

    /**
     * Root directory (relative to the working directory, or absolute)
     * under which all uploaded files are stored, further split into
     * per-category sub-folders, e.g. {basePath}/resumes, {basePath}/logos,
     * {basePath}/profile-pictures.
     */
    private String basePath;

    /**
     * Base path used to build the publicly resolvable URL returned to
     * clients. Must match the FileUploadController "/serve" mapping.
     */
    private String baseUrl;

    private MaxSize maxSize = new MaxSize();

    private AllowedExtensions allowedExtensions = new AllowedExtensions();

    @Getter
    @Setter
    public static class MaxSize {

        /** Maximum resume size, in bytes. */
        private long resume;

        /** Maximum image (logo / profile picture) size, in bytes. */
        private long image;
    }

    @Getter
    @Setter
    public static class AllowedExtensions {

        /** Comma-separated list, e.g. "pdf,doc,docx". */
        private String resume;

        /** Comma-separated list, e.g. "jpg,jpeg,png,webp". */
        private String image;
    }
}