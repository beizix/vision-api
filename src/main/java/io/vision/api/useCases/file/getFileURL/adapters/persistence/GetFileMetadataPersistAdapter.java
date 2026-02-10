package io.vision.api.useCases.file.getFileURL.adapters.persistence;

import io.vision.api.common.adapters.persistence.entity.FileMetadataEntity;
import io.vision.api.common.adapters.persistence.repository.FileMetadataRepository;
import io.vision.api.useCases.file.getFileURL.application.GetFileMetadata;
import io.vision.api.useCases.file.getFileURL.application.domain.model.GetFileURL;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetFileMetadataPersistAdapter implements GetFileMetadata {
  private final FileMetadataRepository fileMetadataRepository;

  @Override
  public GetFileURL operate(UUID fileUuid) {
    FileMetadataEntity metadata = fileMetadataRepository.findById(fileUuid).orElseThrow();
    return new GetFileURL(metadata.getType(), metadata.getPath(), metadata.getName());
  }
}
