package com.hireflow.util;

import java.util.UUID;

/**
 * Small stateless helpers for validating and sanitizing uploaded files.
 * The original client-supplied filename is never trusted beyond reading
 * its extension - a fresh random name is always generated for disk storage,
 * which avoids path traversal, collisions, and unsafe characters.
 */
public final class FileValidationUtil {

    private FileValidationUtil() {
    }

    public static String extractExtension(String originalFileName) {

        if (originalFileName == null || !originalFileName.contains(".")) {
            return "";
        }

        return originalFileName
                .substring(originalFileName.lastIndexOf('.') + 1)
                .trim()
                .toLowerCase();
    }

    public static boolean isExtensionAllowed(String extension, String commaSeparatedWhitelist) {

        if (extension == null || extension.isBlank() || commaSeparatedWhitelist == null) {
            return false;
        }

        for (String allowed : commaSeparatedWhitelist.split(",")) {
            if (allowed.trim().equalsIgnoreCase(extension)) {
                return true;
            }
        }

        return false;
    }

    public static String generateStoredFileName(String extension) {

        String unique = UUID.randomUUID().toString().replace("-", "");

        return (extension == null || extension.isBlank())
                ? unique
                : unique + "." + extension;
    }
}