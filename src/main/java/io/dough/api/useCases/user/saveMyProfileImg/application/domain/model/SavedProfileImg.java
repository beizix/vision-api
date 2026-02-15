package io.dough.api.useCases.user.saveMyProfileImg.application.domain.model;

import io.dough.api.useCases.file.saveFile.application.domain.model.FileUploadType;
import java.util.UUID;

public record SavedProfileImg(
    UUID id,
    String name,
    String originName,
    Long fileLength,
    String referURL) {}
