package io.vision.api.useCases.storage.getFileResource.adapters.storage;

import io.vision.api.useCases.storage.getFileResource.application.GetResourceURLPortOut;
import io.vision.api.useCases.storage.uploadFile.application.model.FileStorageType;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GetS3ResourceURLAdapter implements GetResourceURLPortOut {
  @Value("${app.aws.s3.domain:#{null}}")
  private String cloudFrontDomain;

  @Value("${app.aws.s3.bucketFolder:#{null}}")
  private String bucketFolder;

  @Override
  public FileStorageType getStorageType() {
    return FileStorageType.S3;
  }

  @Override
  public String operate(String path, String filename) {
    return UriComponentsBuilder.fromPath("https://")
        .path("/" + cloudFrontDomain)
        .path("/" + bucketFolder)
        .path("/" + path)
        .path("/" + filename)
        .build()
        .toUriString();
  }
}
