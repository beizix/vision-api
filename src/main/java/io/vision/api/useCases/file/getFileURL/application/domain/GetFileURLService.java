package io.vision.api.useCases.file.getFileURL.application.domain;

import io.vision.api.useCases.file.getFileURL.application.GetFileURLUseCase;
import io.vision.api.useCases.file.getFileURL.application.GetFileMetadata;
import io.vision.api.useCases.file.getFileURL.application.GetFileURL;
import io.vision.api.useCases.file.saveFile.application.domain.model.FileStorageType;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetFileURLService implements GetFileURLUseCase {
  private final GetFileMetadata getFileMetadata;
  private final Set<GetFileURL> getResourceURLStrategies;

  @Override
  public String operate(UUID fileUuid) {
    io.vision.api.useCases.file.getFileURL.application.domain.model.GetFileURL fileResource = getFileMetadata.operate(fileUuid);
    return getResourceURLStrategy(fileResource.fileUploadType().getFileStorageType())
        .operate(fileResource.path(), fileResource.filename());
  }

  private GetFileURL getResourceURLStrategy(FileStorageType fileStorageType) {
    return getResourceURLStrategies.stream()
        .filter(
          getFileURL -> getFileURL.getStorageType().equals(fileStorageType))
        .findFirst()
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    String.format("No resource url strategy found: %s", fileStorageType.name())));
  }
}
