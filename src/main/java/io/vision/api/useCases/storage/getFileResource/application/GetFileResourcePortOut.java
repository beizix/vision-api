package io.vision.api.useCases.storage.getFileResource.application;

import io.vision.api.useCases.storage.getFileResource.application.model.GetFileResource;
import java.util.UUID;

public interface GetFileResourcePortOut {
  GetFileResource operate(UUID fileUuid);
}
