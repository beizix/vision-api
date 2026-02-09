package io.vision.api.useCases.storage.uploadFile.application.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FileUploadType {
  // 사용자 프로필 이미지
  USER_IMAGE(FileStorageType.LOCAL, "/user/images", Set.of(AcceptableFileType.IMAGE)),
  MANAGER_IMAGE(FileStorageType.S3, "/manager/images", Set.of(AcceptableFileType.IMAGE)),
  // WEB Editor 이미지 파일
  EDITOR_IMAGE(FileStorageType.LOCAL, "/editor/images", Set.of(AcceptableFileType.IMAGE));

  private final FileStorageType fileStorageType;
  private final String subPath;
  private final Set<AcceptableFileType> acceptableFileTypes;

  private String name;

  public String getName() {
    return name();
  }
}
