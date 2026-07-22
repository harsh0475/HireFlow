package com.hireflow.entity.enums;

/**
 * Identifies the kind of file being uploaded through the File Upload
 * Module. Each category maps to its own on-disk sub-folder and to a
 * validation rule-set (max size / allowed extensions) defined in
 * {@code app.file.*} (see {@link com.hireflow.config.FileStorageProperties}).
 */
public enum UploadCategory {

    RESUME("resumes", Kind.RESUME),
    LOGO("logos", Kind.IMAGE),
    PROFILE_PICTURE("profile-pictures", Kind.IMAGE);

    private final String folder;
    private final Kind kind;

    UploadCategory(String folder, Kind kind) {
        this.folder = folder;
        this.kind = kind;
    }

    public String getFolder() {
        return folder;
    }

    public Kind getKind() {
        return kind;
    }

    /**
     * Groups categories that share the same validation rule-set
     * (a resume is validated against document extensions/size, while a
     * logo and a profile picture are both validated as images).
     */
    public enum Kind {
        RESUME,
        IMAGE
    }
}