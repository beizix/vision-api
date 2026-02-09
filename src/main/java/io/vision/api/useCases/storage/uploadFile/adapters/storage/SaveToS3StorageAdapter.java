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

  @Value("${app.aws.s3.bucketName:#{null}}")
  private String bucketName;

  @Value("${app.aws.s3.bucketFolder:#{null}}")
  private String bucketFolder;

  @Override
  public FileStorageType getStorageType() {
    return FileStorageType.S3;
  }

  @Override
  public void operate(InputStream inputStream, String createSubPath, String createFilename)
      throws IOException {

    // Path.of와 normalize를 사용하여 중복 슬래시 방지 및 경로 정규화
    // URL이므로 윈도우 스타일 역슬래시(\)를 슬래시(/)로 변환
    String combinedPath =
        Paths.get(createSubPath, createFilename).normalize().toString().replace("\\", "/");

    String path =
        UriComponentsBuilder.fromPath(bucketFolder).path("/" + combinedPath).build().toUriString();

    s3Template.upload(bucketName, path, inputStream);
  }
}
