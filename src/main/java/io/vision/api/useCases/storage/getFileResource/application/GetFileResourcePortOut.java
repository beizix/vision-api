package io.vision.api.useCases.storage.getFileResource.application;

import io.vision.api.useCases.storage.getFileResource.application.model.GetFileResource;

public interface GetFileResourcePortOut {
  GetFileResource operate(String fileUuid);
}
