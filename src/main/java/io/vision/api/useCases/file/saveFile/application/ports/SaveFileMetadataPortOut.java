package io.vision.api.useCases.file.saveFile.application.ports;

import io.vision.api.useCases.file.saveFile.application.domain.model.SaveFileMetadata;
import io.vision.api.useCases.file.saveFile.application.domain.model.SaveFileMetadataCmd;

import java.util.Optional;

/**
 * 애플리케이션 계층에서 영속성 계층으로 파일 메타데이터 저장을 요청하는 출력 포트입니다.
 */
public interface SaveFileMetadataPortOut {

  /**
   * 파일 메타데이터를 영구 저장소에 기록합니다.
   *
   * @param metadataCmd 저장할 파일 메타데이터 정보 (파일명, 경로, 크기 등)
   * @return 저장 성공 시 생성된 메타데이터 정보(ID 포함), DB 제약 조건 위반 등으로 저장 실패 시 Optional.empty()
   */
  Optional<SaveFileMetadata> operate(SaveFileMetadataCmd metadataCmd);
}
