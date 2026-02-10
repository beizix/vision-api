package io.vision.api.useCases.file.saveFile.application;

import io.vision.api.useCases.file.saveFile.application.domain.model.FileStorageType;

import java.io.IOException;
import java.io.InputStream;

/**
 * 애플리케이션 계층에서 파일 저장소(로컬, S3 등)로 실제 파일 물리 저장을 요청하는 출력 포트입니다.
 */
public interface SaveToFileStorage {
  /**
   * 현재 포트 구현체가 지원하는 저장소 유형을 반환합니다.
   *
   * @return 저장소 유형 (LOCAL, S3 등)
   */
  FileStorageType getStorageType();

  /**
   * 입력 스트림의 데이터를 지정된 경로와 파일명으로 저장소에 저장합니다.
   *
   * @param inputStream    저장할 파일의 데이터 스트림
   * @param createSubPath  저장소 내의 하위 경로 (예: 년/월/일)
   * @param createFilename 저장소에 생성될 파일명
   * @throws IOException 파일 저장 중 입출력 오류 발생 시
   */
  void operate(InputStream inputStream, String createSubPath, String createFilename)
      throws IOException;
}
