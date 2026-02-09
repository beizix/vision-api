package io.vision.api.useCases.storage.uploadFile.adapters.storage;

import io.awspring.cloud.s3.S3Template;
import io.vision.api.useCases.storage.uploadFile.application.SaveToFileStoragePortOut;
import io.vision.api.useCases.storage.uploadFile.application.model.FileStorageType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class SaveToS3StorageAdapter implements SaveToFileStoragePortOut {
  private final S3Template s3Template;

  @Value("${spring.cloud.aws.s3.bucket:#{null}}")
  private String bucketName;

  @Value("${spring.cloud.aws.s3.folder:#{null}}")
  private String bucketFolder;

  @Override
  public FileStorageType getStorageType() {
    return FileStorageType.S3;
  }

  @Override
  public void operate(InputStream inputStream, String createSubPath, String createFilename)
      throws IOException {

    String path =
        UriComponentsBuilder.fromPath(bucketFolder)
            .path("/" + createSubPath)
            .path("/" + createFilename)
            .build()
            .toUriString();

    s3Template.upload(bucketName, path, inputStream);
  }
}
