package io.vision.api.useCases.storage.getFileResource.application;

import java.util.UUID;

public interface GetFileResourceUseCase {
  String operate(UUID fileUuid);
}
