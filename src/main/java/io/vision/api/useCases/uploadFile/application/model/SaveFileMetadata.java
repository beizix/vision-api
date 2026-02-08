package io.vision.api.useCases.uploadFile.application.model;

import java.util.UUID;

public record SaveFileMetadata(
    UUID id, FileUploadType type, String path, String name, String originName, Long fileLength) {}
