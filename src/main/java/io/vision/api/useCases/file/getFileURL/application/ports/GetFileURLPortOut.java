package io.vision.api.useCases.file.getFileURL.application.ports;

import io.vision.api.useCases.file.saveFile.application.domain.model.FileStorageType;

/**
 * 애플리케이션 계층에서 저장소 계층(로컬, S3 등)으로 파일의 접근 URL 생성을 요청하는 출력 포트입니다.
 */
public interface GetFileURLPortOut {
  /**
   * 현재 포트 구현체가 지원하는 저장소 유형을 반환합니다.
   *
   * @return 저장소 유형 (LOCAL, S3 등)
   */
  FileStorageType getStorageType();

  /**
   * 저장된 경로와 파일명을 조합하여 접근 가능한 URL을 생성합니다.
   *
   * @param path     저장소 내의 파일 경로
   * @param filename 저장소에 저장된 파일명
   * @return 생성된 파일 접근 URL
   */
  String operate(String path, String filename);
}
