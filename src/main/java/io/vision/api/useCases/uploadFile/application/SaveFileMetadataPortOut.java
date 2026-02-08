package io.vision.api.useCases.uploadFile.application;

import io.vision.api.useCases.uploadFile.application.model.SaveFileMetadata;
import io.vision.api.useCases.uploadFile.application.model.SaveFileMetadataCmd;
import java.util.Optional;

public interface SaveFileMetadataPortOut {
  Optional<SaveFileMetadata> operate(SaveFileMetadataCmd metadataCmd);
}
