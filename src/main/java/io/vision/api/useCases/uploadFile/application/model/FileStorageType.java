package io.vision.api.useCases.uploadFile.application.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileStorageType {
  LOCAL("Local Storage"),
  S3("AWS S3 Storage");

  private final String desc;
}
