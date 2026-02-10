package io.vision.api.useCases.file.getFileURL.application;

import io.vision.api.useCases.file.getFileURL.application.domain.model.GetFileURL;
import java.util.UUID;

/**
 * 애플리케이션 계층에서 영속성 계층으로 파일 메타데이터 조회를 요청하는 출력 포트입니다.
 */
public interface GetFileMetadata {
  /**
   * 파일 고유 식별자를 통해 파일 메타데이터 정보를 조회합니다.
   *
   * @param fileUuid 파일 고유 식별자 (UUID)
   * @return 조회된 파일 메타데이터 정보
   */
  GetFileURL operate(UUID fileUuid);
}
