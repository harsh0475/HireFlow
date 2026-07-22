package com.hireflow.service.impl;

import com.hireflow.config.FileStorageProperties;
import com.hireflow.entity.enums.UploadCategory;
import com.hireflow.exception.BadRequestException;
import com.hireflow.exception.ResourceNotFoundException;
import com.hireflow.service.FileStorageService;
import com.hireflow.service.StoredFileInfo;
import com.hireflow.util.FileValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Local-disk implementation of {@link FileStorageService}. Base path and
 * per-category limits/whitelists are configurable via {@code app.file.*}
 * in application.yaml (see {@link FileStorageProperties}).
 *
 * To migrate to AWS S3 later, implement {@link FileStorageService} against
 * the S3 SDK and swap the {@code @Service} bean - FileUploadService and
 * every controller stay untouched.
 */
@Service
@RequiredArgsConstructor
public class LocalFileStorageServiceImpl implements FileStorageService {

    private final FileStorageProperties fileStorageProperties;

    @Override
    public StoredFileInfo store(MultipartFile file, UploadCategory category) {

        validateNotEmpty(file);
        validateSize(file, category);
        String extension = validateExtension(file, category);

        try {
            Path categoryDir = resolveCategoryDir(category);
            Files.createDirectories(categoryDir);

            String storedFileName = FileValidationUtil.generateStoredFileName(extension);
            Path targetPath = categoryDir.resolve(storedFileName).normalize();

            if (!targetPath.startsWith(categoryDir)) {
                throw new BadRequestException("Invalid file name.");
            }

            try (var inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            String relativePath = category.getFolder() + "/" + storedFileName;

            return new StoredFileInfo(
                    relativePath,
                    buildPublicUrl(relativePath),
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize()
            );

        } catch (IOException ex) {
            throw new RuntimeException("Failed to store uploaded file.", ex);
        }
    }

    @Override
    public void delete(String relativePath) {

        if (relativePath == null || relativePath.isBlank()) {
            return;
        }

        try {
            Path basePath = resolveBasePath();
            Path target = basePath.resolve(relativePath).normalize();

            if (!target.startsWith(basePath)) {
                return;
            }

            Files.deleteIfExists(target);

        } catch (IOException ex) {
            throw new RuntimeException("Failed to delete stored file.", ex);
        }
    }

    @Override
    public Resource loadAsResource(String relativePath) {

        try {
            Path basePath = resolveBasePath();
            Path target = basePath.resolve(relativePath).normalize();

            if (!target.startsWith(basePath)) {
                throw new ResourceNotFoundException("File not found.");
            }

            Resource resource = new UrlResource(target.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("File not found.");
            }

            return resource;

        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File not found.");
        }
    }

    private void validateNotEmpty(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Please select a file to upload.");
        }
    }

    private void validateSize(MultipartFile file, UploadCategory category) {

        long max = category.getKind() == UploadCategory.Kind.RESUME
                ? fileStorageProperties.getMaxSize().getResume()
                : fileStorageProperties.getMaxSize().getImage();

        if (max > 0 && file.getSize() > max) {
            throw new BadRequestException(
                    "File exceeds the maximum allowed size of " + (max / (1024 * 1024)) + "MB.");
        }
    }

    private String validateExtension(MultipartFile file, UploadCategory category) {

        String extension = FileValidationUtil.extractExtension(file.getOriginalFilename());

        String whitelist = category.getKind() == UploadCategory.Kind.RESUME
                ? fileStorageProperties.getAllowedExtensions().getResume()
                : fileStorageProperties.getAllowedExtensions().getImage();

        if (!FileValidationUtil.isExtensionAllowed(extension, whitelist)) {
            throw new BadRequestException("Unsupported file type. Allowed types: " + whitelist);
        }

        return extension;
    }

    private Path resolveCategoryDir(UploadCategory category) {
        return resolveBasePath().resolve(category.getFolder());
    }

    private Path resolveBasePath() {
        return Paths.get(fileStorageProperties.getBasePath()).toAbsolutePath().normalize();
    }

    private String buildPublicUrl(String relativePath) {

        String baseUrl = fileStorageProperties.getBaseUrl();

        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        return baseUrl + "/" + relativePath;
    }
}