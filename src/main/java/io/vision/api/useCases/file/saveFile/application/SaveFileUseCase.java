package io.vision.api.useCases.file.saveFile.application;

import io.vision.api.useCases.file.saveFile.application.domain.model.FileUploadType;
import io.vision.api.useCases.file.saveFile.application.domain.model.SaveFile;

import java.io.InputStream;
import java.util.Optional;

/**
 * 주어진 입력 스트림을 비즈니스 규칙에 따라 저장소에 업로드하고, 파일 메타데이터를 생성합니다.
 */
public interface SaveFileUseCase {

  /**
   * 파일을 업로드하고 처리 결과를 반환합니다.
   *
   * @param fileUploadType   업로드 목적/유형 (이미지, 일반 문서 등)
   * @param inputStream      파일 데이터 스트림
   * @param originalFilename 원본 파일명
   * @param fileSize         파일 크기 (byte)
   * @return 업로드 성공 시 생성된 파일 정보, 정책상 업로드가 거부되거나 실패 시 Optional.empty()
   */
  Optional<SaveFile> operate(
      FileUploadType fileUploadType,
      InputStream inputStream,
      String originalFilename,
      long fileSize);
}
