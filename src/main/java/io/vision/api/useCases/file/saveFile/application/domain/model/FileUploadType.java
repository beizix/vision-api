package io.vision.api.useCases.file.saveFile.application.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FileUploadType {
  UPLOAD_IMG_TO_LOCAL(FileStorageType.LOCAL, "/localTest/img", Set.of(AcceptableFileType.IMAGE)),
  UPLOAD_IMG_TO_S3(FileStorageType.S3, "/s3Test/img", Set.of(AcceptableFileType.IMAGE));

  private final FileStorageType fileStorageType;
  private final String subPath;
  private final Set<AcceptableFileType> acceptableFileTypes;

  public String getName() {
    return name();
  }
}
