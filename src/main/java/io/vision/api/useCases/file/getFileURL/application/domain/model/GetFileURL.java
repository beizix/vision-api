package io.vision.api.useCases.file.getFileURL.application.domain.model;

import io.vision.api.useCases.file.saveFile.application.domain.model.FileUploadType;

public record GetFileURL(FileUploadType fileUploadType, String path, String filename) {}
