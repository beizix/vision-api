package io.vision.api.useCases.file.getFileURL.application.domain;

import io.vision.api.useCases.file.getFileURL.application.GetFileURLUseCase;
import io.vision.api.useCases.file.getFileURL.application.domain.model.GetFileURL;
import io.vision.api.useCases.file.getFileURL.application.ports.GetFileMetadataPortOut;
import io.vision.api.useCases.file.getFileURL.application.ports.GetFileURLPortOut;
import io.vision.api.useCases.file.saveFile.application.domain.model.FileStorageType;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetFileURLService implements GetFileURLUseCase {
  private final GetFileMetadataPortOut getFileMetadataPortOut;
  private final Set<GetFileURLPortOut> getResourceURLStrategies;

  @Override
  public String operate(UUID fileUuid) {
    GetFileURL fileResource = getFileMetadataPortOut.operate(fileUuid);
    return getResourceURLStrategy(fileResource.fileUploadType().getFileStorageType())
        .operate(fileResource.path(), fileResource.filename());
  }

  private GetFileURLPortOut getResourceURLStrategy(FileStorageType fileStorageType) {
    return getResourceURLStrategies.stream()
        .filter(
          getFileURLPortOut -> getFileURLPortOut.getStorageType().equals(fileStorageType))
        .findFirst()
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    String.format("No resource url strategy found: %s", fileStorageType.name())));
  }
}
