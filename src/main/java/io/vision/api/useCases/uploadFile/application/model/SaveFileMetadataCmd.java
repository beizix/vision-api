package io.vision.api.useCases.uploadFile.application.model;

public record SaveFileMetadataCmd(
    FileUploadType type, String path, String name, String originName, Long fileLength) {}
