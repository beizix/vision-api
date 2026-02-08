package io.vision.api.common.adapters.persistence.entity;

import io.vision.api.common.adapters.persistence.component.AuditEntity;
import io.vision.api.useCases.storage.uploadFile.application.model.FileUploadType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "fileMetadata", comment = "업로드 파일 메타데이터 테이블")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction(value = "deleted = false")
public class FileMetadataEntity extends AuditEntity {
  @Id
  @Column(nullable = false, length = 36, comment = "파일 UUID")
  private UUID id;

  @Column(nullable = false, length = 255, comment = "파일 업로드 타입")
  @Enumerated(EnumType.STRING)
  private FileUploadType type;

  @Column(nullable = false, comment = "파일 경로")
  private String path;

  @Column(nullable = false, comment = "파일 이름")
  private String name;

  @Column(nullable = false, comment = "원본 파일 이름")
  private String originName;

  @Column(nullable = false, comment = "파일 크기")
  private Long fileLength;
}
