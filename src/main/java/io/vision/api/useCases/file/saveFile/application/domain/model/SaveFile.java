package io.vision.api.useCases.file.saveFile.application.domain.model;

import java.util.UUID;

/**
 * 저장 완료된 파일 정보를 담은 도메인 모델입니다.
 *
 * @param id         파일 고유 식별자
 * @param type       파일 업로드 유형
 * @param path       저장된 경로
 * @param name       저장된 파일명
 * @param originName 원본 파일명
 * @param fileLength 파일 크기
 */
public record SaveFile(
  UUID id,
  FileUploadType type,
  String path,
  String name,
  String originName,
  Long fileLength
) {
}
