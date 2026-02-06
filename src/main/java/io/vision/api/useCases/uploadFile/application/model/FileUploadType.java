package io.vision.api.useCases.uploadFile.application.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FileUploadType {
  // 사용자 프로필 이미지
  USER_IMAGE(FileStorageType.LOCAL, "/ogImage", Set.of(AcceptableFileType.IMAGE)),
  // WEB Editor 이미지 파일
  EDITOR_IMAGE(FileStorageType.LOCAL, "/editorImage", Set.of(AcceptableFileType.IMAGE));

  private final FileStorageType fileStorageType;
  private final String subPath;
  private final Set<AcceptableFileType> acceptableFileTypes;

  private String name;

  public String getName() {
    return name();
  }
}
