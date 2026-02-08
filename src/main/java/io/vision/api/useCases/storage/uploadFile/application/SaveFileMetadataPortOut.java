package io.vision.api.useCases.storage.uploadFile.application;

import io.vision.api.useCases.storage.uploadFile.application.model.SaveFileMetadata;
import io.vision.api.useCases.storage.uploadFile.application.model.SaveFileMetadataCmd;
import java.util.Optional;

public interface SaveFileMetadataPortOut {
  Optional<SaveFileMetadata> operate(SaveFileMetadataCmd metadataCmd);
}
