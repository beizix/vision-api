package io.vision.api.useCases.storage.getFileResource.application;

import io.vision.api.useCases.storage.getFileResource.application.model.GetFileResource;
import io.vision.api.useCases.storage.uploadFile.application.model.FileStorageType;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetFileResourceService implements GetFileResourceUseCase {
  private final GetFileResourcePortOut getFileResourcePortOut;
  private final Set<GetResourceURLPortOut> getResourceURLStrategies;

  @Override
  public String operate(UUID fileUuid) {
    GetFileResource fileResource = getFileResourcePortOut.operate(fileUuid);
    return getResourceURLStrategy(FileStorageType.LOCAL)
        .operate(fileResource.path(), fileResource.filename());
  }

  private GetResourceURLPortOut getResourceURLStrategy(FileStorageType fileStorageType) {
    return getResourceURLStrategies.stream()
        .filter(
            getResourceURLPortOut -> getResourceURLPortOut.getStorageType().equals(fileStorageType))
        .findFirst()
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    String.format("No resource url strategy found: %s", fileStorageType.name())));
  }
}
