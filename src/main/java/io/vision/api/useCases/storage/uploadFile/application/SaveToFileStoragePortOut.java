package io.vision.api.useCases.storage.uploadFile.application;

import io.vision.api.useCases.storage.uploadFile.application.model.FileStorageType;
import java.io.IOException;
import java.io.InputStream;

public interface SaveToFileStoragePortOut {
  FileStorageType getStorageType();

  void operate(InputStream inputStream, String createSubPath, String createFilename)
      throws IOException;
}
