package io.vision.api.useCases.uploadFile.application;

import io.vision.api.useCases.uploadFile.application.model.FileUploadType;
import io.vision.api.useCases.uploadFile.application.model.UploadFile;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileUseCase {
  Optional<UploadFile> operate(FileUploadType fileUploadType, MultipartFile file);
}
