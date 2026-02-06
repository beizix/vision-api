package io.vision.api.useCases.uploadFile.application;

import io.vision.api.useCases.uploadFile.application.model.FileUploadType;
import io.vision.api.useCases.uploadFile.application.model.UploadFile;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadFileService implements UploadFileUseCase {
  @Override
  public Optional<UploadFile> operate(FileUploadType fileUploadType, MultipartFile file) {
    return Optional.empty();
  }

  @Override
  public Optional<UploadFile> operate(FileUploadType fileUploadType, String base64String) {
    return Optional.empty();
  }
}
