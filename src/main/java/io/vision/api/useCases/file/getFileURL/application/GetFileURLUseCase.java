package io.vision.api.useCases.file.getFileURL.application;

import java.util.UUID;

/**
 * 특정 파일의 접근 가능한 URL을 조회합니다.
 */
public interface GetFileURLUseCase {

  /**
   * 파일의 고유 식별자를 통해 접근 URL을 반환합니다.
   *
   * @param fileUuid 파일 고유 식별자 (UUID)
   * @return 파일이 존재할 경우 URL 문자열, 그렇지 않을 경우 예외 발생
   */
  String operate(UUID fileUuid);
}
