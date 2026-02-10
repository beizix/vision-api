package io.vision.api.useCases.file.saveFile.adapters.persistence;

import io.vision.api.common.adapters.persistence.entity.FileMetadataEntity;
import io.vision.api.common.adapters.persistence.repository.FileMetadataRepository;
import io.vision.api.useCases.file.saveFile.application.ports.SaveFileMetadataPortOut;
import io.vision.api.useCases.file.saveFile.application.domain.model.SaveFileMetadata;
import io.vision.api.useCases.file.saveFile.application.domain.model.SaveFileMetadataCmd;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveFileMetadataPersistAdapter implements SaveFileMetadataPortOut {
  private final FileMetadataRepository fileMetadataRepo;

  @Override
  public Optional<SaveFileMetadata> operate(SaveFileMetadataCmd cmd) {
    FileMetadataEntity fileMetadata =
        fileMetadataRepo.save(
            new FileMetadataEntity(
                UUID.randomUUID(),
                cmd.type(),
                cmd.path(),
                cmd.name(),
                cmd.originName(),
                cmd.fileLength()));

    return Optional.of(
        new SaveFileMetadata(
            fileMetadata.getId(),
            fileMetadata.getType(),
            fileMetadata.getPath(),
            fileMetadata.getName(),
            fileMetadata.getOriginName(),
            fileMetadata.getFileLength()));
  }
}
