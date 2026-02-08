package io.vision.api.useCases.storage.uploadFile.adapters.web.model;

import java.util.UUID;

public record UploadFileRes(
    UUID id, String path, String name, String originName, String referURL) {}
