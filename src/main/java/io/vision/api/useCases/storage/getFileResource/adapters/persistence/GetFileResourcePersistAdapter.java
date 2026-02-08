package io.vision.api.useCases.storage.getFileResource.adapters.persistence;

import io.vision.api.common.adapters.persistence.entity.FileMetadataEntity;
import io.vision.api.common.adapters.persistence.repository.FileMetadataRepository;
import io.vision.api.useCases.storage.getFileResource.application.GetFileResourcePortOut;
import io.vision.api.useCases.storage.getFileResource.application.model.GetFileResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetFileResourcePersistAdapter implements GetFileResourcePortOut {
  private final FileMetadataRepository fileMetadataRepository;

  @Override
  public GetFileResource operate(String fileUuid) {
    FileMetadataEntity metadata = fileMetadataRepository.findById(fileUuid).orElseThrow();
    return new GetFileResource(metadata.getPath(), metadata.getName());
  }
}
