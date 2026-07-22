package com.hireflow.service;

import com.hireflow.entity.enums.UploadCategory;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Abstraction over physical file storage. The current implementation
 * ({@code LocalFileStorageServiceImpl}) stores files on local disk.
 * To migrate to AWS S3 (or any other backend) later, only a new
 * implementation of this interface needs to be written and wired in
 * place of the local one - callers (FileUploadService) never talk to
 * the filesystem directly.
 */
public interface FileStorageService {

    /**
     * Validates (type + size) and stores the given multipart file under
     * the folder dedicated to the given category, returning metadata
     * about the stored file including a publicly resolvable URL.
     */
    StoredFileInfo store(MultipartFile file, UploadCategory category);

    /**
     * Deletes a previously stored file, given the relative path returned
     * inside {@link StoredFileInfo#relativePath()} at store time.
     * Silently does nothing if the file no longer exists.
     */
    void delete(String relativePath);

    /**
     * Loads a previously stored file as a Spring {@link Resource}, for
     * serving it back over HTTP.
     */
    Resource loadAsResource(String relativePath);
}