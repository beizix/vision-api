package io.vision.api.useCases.file.getFileURL.adapters.storage;

import io.vision.api.useCases.file.getFileURL.application.GetFileURL;
import io.vision.api.useCases.file.saveFile.application.domain.model.FileStorageType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GetS3FileURLAdapter implements GetFileURL {
  @Value("${spring.cloud.aws.s3.domain:#{null}}")
  private String cloudFrontDomain;

  @Value("${spring.cloud.aws.s3.folder:#{null}}")
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
