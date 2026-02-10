package io.vision.api.useCases.file.saveFile.adapters.web.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.vision.api.useCases.file.saveFile.application.domain.model.FileUploadType;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UploadMultipartReq(
    @Schema(description = "업로드 파일 타입", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    FileUploadType type,

    @Schema(description = "업로드할 파일", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    MultipartFile file
) {}
