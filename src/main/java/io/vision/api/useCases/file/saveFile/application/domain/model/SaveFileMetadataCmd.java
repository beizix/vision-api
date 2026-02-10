package io.vision.api.useCases.file.saveFile.application.domain.model;

/**
 * 파일 메타데이터 저장을 위한 커맨드 객체입니다.
 *
 * @param type       파일 업로드 유형
 * @param path       저장소 내 하위 경로
 * @param name       저장된 파일명
 * @param originName 원본 파일명
 * @param fileLength 파일 크기 (bytes)
 */
public record SaveFileMetadataCmd(
  FileUploadType type, String path, String name, String originName, Long fileLength) {
}
