package io.vision.api.useCases.storage.getFileResource.adapters.storage;

import io.vision.api.useCases.storage.getFileResource.application.GetResourceURLPortOut;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.vision.api.useCases.storage.uploadFile.application.model.FileStorageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

    // Path.of와 normalize를 사용하여 중복 슬래시 방지 및 경로 정규화
    // URL이므로 윈도우 스타일 역슬래시(\)를 슬래시(/)로 변환
    String combinedPath = Paths.get(path, filename).normalize().toString().replace("\\", "/");

    // 시작 부분에 슬래시가 있으면 제거하여 중복 방지
    if (combinedPath.startsWith("/")) {
      combinedPath = combinedPath.substring(1);
    }

    return URL_PREFIX + combinedPath;
  }
}
