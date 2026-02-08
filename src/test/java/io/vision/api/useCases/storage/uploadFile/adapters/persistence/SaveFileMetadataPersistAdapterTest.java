package io.vision.api.useCases.storage.uploadFile.adapters.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.vision.api.common.adapters.persistence.entity.FileMetadataEntity;
import io.vision.api.common.adapters.persistence.repository.FileMetadataRepository;
import io.vision.api.support.DataJpaTestBase;
import io.vision.api.useCases.storage.uploadFile.application.model.FileUploadType;
import io.vision.api.useCases.storage.uploadFile.application.model.SaveFileMetadata;
import io.vision.api.useCases.storage.uploadFile.application.model.SaveFileMetadataCmd;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(SaveFileMetadataPersistAdapter.class)
class SaveFileMetadataPersistAdapterTest extends DataJpaTestBase {

  @Autowired
  private SaveFileMetadataPersistAdapter adapter;

  @Autowired
  private FileMetadataRepository repository;

  @Test
  @DisplayName("Scenario: 성공 - 파일 메타데이터를 DB에 저장한다")
  void save_file_metadata_success() {
    // Given
    SaveFileMetadataCmd cmd = new SaveFileMetadataCmd(
        FileUploadType.USER_IMAGE,
        "/path/202602",
        "uuid-name.png",
        "original.png",
        1024L
    );

    // When
    Optional<SaveFileMetadata> result = adapter.operate(cmd);

    // Then
    assertThat(result).isPresent();
    SaveFileMetadata saved = result.get();
    assertThat(saved.id()).isNotNull();
    assertThat(saved.type()).isEqualTo(cmd.type());
    assertThat(saved.path()).isEqualTo(cmd.path());
    assertThat(saved.name()).isEqualTo(cmd.name());
    assertThat(saved.originName()).isEqualTo(cmd.originName());
    assertThat(saved.fileLength()).isEqualTo(cmd.fileLength());

    // DB 직접 검증
    FileMetadataEntity entity = repository.findById(saved.id()).orElseThrow();
    assertThat(entity.getName()).isEqualTo(cmd.name());
    assertThat(entity.getCreatedAt()).isNotNull(); // AuditEntity 동작 확인
  }
}
