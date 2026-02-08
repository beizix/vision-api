package io.vision.api.useCases.uploadFile.adapters.web.model;

import io.vision.api.useCases.uploadFile.application.model.FileUploadType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UploadBase64Req(
    @Schema(description = "업로드 파일 타입", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    FileUploadType type,

    @Schema(description = "Base64 인코딩된 파일 데이터", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    String base64Data
) {}
