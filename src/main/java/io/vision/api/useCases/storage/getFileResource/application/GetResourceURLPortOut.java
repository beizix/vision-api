package io.vision.api.useCases.storage.getFileResource.application;

import io.vision.api.useCases.storage.uploadFile.application.model.FileStorageType;

import java.util.UUID;

public interface GetResourceURLPortOut {
  FileStorageType getStorageType();
  String operate(String path, String filename);
}
