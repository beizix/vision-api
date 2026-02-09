package io.vision.api.useCases.storage.getFileResource.adapters.storage;

import io.vision.api.useCases.storage.getFileResource.application.GetResourceURLPortOut;
import io.vision.api.useCases.storage.uploadFile.application.model.FileStorageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GetLocalResourceURLAdapter implements GetResourceURLPortOut {

  private static final String URL_PREFIX = "/uploads/";

  @Override
  public FileStorageType getStorageType() {
    return FileStorageType.LOCAL;
  }

  @Override
  public String operate(String path, String filename) {
    if (path == null || filename == null) {
      return "";
    }

    return UriComponentsBuilder.fromPath(URL_PREFIX)
        .path("/" + path)
        .path("/" + filename)
        .build()
        .toUriString();
  }
}
