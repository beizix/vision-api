package io.vision.api.useCases.uploadFile.adapters.web.model;

import io.vision.api.useCases.uploadFile.application.model.UploadFile;
import java.util.UUID;

public record UploadFileRes(
    UUID id,
    String path,
    String name,
    String originName,
    String referURL
) {
    public static UploadFileRes from(UploadFile uploadFile) {
        return new UploadFileRes(
            uploadFile.getId(),
            uploadFile.getPath(),
            uploadFile.getName(),
            uploadFile.getOriginName(),
            uploadFile.getReferURL()
        );
    }
}
