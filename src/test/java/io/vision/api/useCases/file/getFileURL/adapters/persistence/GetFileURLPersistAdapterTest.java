package io.vision.api.useCases.file.getFileURL.adapters.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.vision.api.common.adapters.persistence.entity.FileMetadataEntity;
import io.vision.api.common.adapters.persistence.repository.FileMetadataRepository;
import io.vision.api.support.DataJpaTestBase;
import io.vision.api.useCases.file.getFileURL.application.domain.model.GetFileURL;
import io.vision.api.useCases.file.saveFile.application.domain.model.FileUploadType;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(GetFileMetadataPersistAdapter.class)
class GetFileURLPersistAdapterTest extends DataJpaTestBase {

  @Autowired
  private GetFileMetadataPersistAdapter adapter;

  @Autowired
  private FileMetadataRepository repository;

  @Test
  @DisplayName("Scenario: 성공 - 파일 UUID로 리소스 정보를 조회한다")
  void get_file_resource_success() {
    // Given
    UUID fileId = UUID.randomUUID();
    FileUploadType type = FileUploadType.UPLOAD_IMG_TO_LOCAL;
    String path = "/images/202602";
    String name = "uuid-image.png";

    FileMetadataEntity entity = FileMetadataEntity.builder()
        .id(fileId)
        .type(type)
        .path(path)
        .name(name)
        .originName("original.png")
        .fileLength(1024L)
        .build();

    repository.save(entity);

    // When
    GetFileURL result = adapter.operate(fileId);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.fileUploadType()).isEqualTo(type);
    assertThat(result.path()).isEqualTo(path);
    assertThat(result.filename()).isEqualTo(name);
  }

  @Test
  @DisplayName("Scenario: 실패 - 존재하지 않는 파일 UUID 조회 시 예외 발생")
  void get_file_resource_fail_not_found() {
    // Given
    UUID invalidId = UUID.randomUUID();

    // When & Then
    assertThatThrownBy(() -> adapter.operate(invalidId))
        .isInstanceOf(NoSuchElementException.class);
  }
}
