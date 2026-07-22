package com.hireflow.service;

/**
 * Metadata returned by {@link FileStorageService#store} after a file has
 * been validated and persisted.
 *
 * @param relativePath     path relative to the storage root, e.g. "resumes/abc123.pdf" -
 *                          this is what must be passed back into
 *                          {@link FileStorageService#delete} or
 *                          {@link FileStorageService#loadAsResource}.
 * @param publicUrl        fully-formed URL clients can use to fetch the file
 *                          back (via FileUploadController's "/serve" endpoint).
 * @param originalFileName the original, client-supplied file name (for display only).
 * @param contentType       the browser/client reported MIME type.
 * @param sizeInBytes       the file size, in bytes.
 */
public record StoredFileInfo(
        String relativePath,
        String publicUrl,
        String originalFileName,
        String contentType,
        long sizeInBytes
) {
}