package io.vision.api.useCases.file.getFileURL.adapters.storage;

import io.vision.api.useCases.file.getFileURL.application.ports.GetFileURLPortOut;
import io.vision.api.useCases.file.saveFile.application.domain.model.FileStorageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GetLocalFileURLAdapter implements GetFileURLPortOut {

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
