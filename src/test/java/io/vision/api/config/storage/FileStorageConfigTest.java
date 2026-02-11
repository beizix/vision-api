package io.vision.api.config.storage;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

class FileStorageConfigTest {

  private FileStorageConfig fileStorageConfig;

  @TempDir
  Path tempDir;

  @BeforeEach
  void setUp() {
    fileStorageConfig = new FileStorageConfig();
  }

  @Test
  @DisplayName("Scenario: 성공 - 설정된 경로가 존재하면 예외 없이 초기화된다")
  void initialize_success_when_path_exists() {
    // Given
    Path uploadPath = tempDir.resolve("upload");
    // 실제 경로 생성 (테스트 환경이므로)
    uploadPath.toFile().mkdirs();

    ReflectionTestUtils.setField(fileStorageConfig, "uploadPath", uploadPath.toString());

    // When & Then
    assertThatCode(() -> fileStorageConfig.initialize())
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Scenario: 실패 - uploadPath가 null이면 IllegalStateException이 발생한다")
  void initialize_fail_when_path_is_null() {
    // Given
    ReflectionTestUtils.setField(fileStorageConfig, "uploadPath", null);

    // When & Then
    assertThatThrownBy(() -> fileStorageConfig.initialize())
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("필수 설정인 'app.upload.path'가 누락되었습니다.");
  }

  @Test
  @DisplayName("Scenario: 실패 - uploadPath가 빈 문자열이면 IllegalStateException이 발생한다")
  void initialize_fail_when_path_is_blank() {
    // Given
    ReflectionTestUtils.setField(fileStorageConfig, "uploadPath", " ");

    // When & Then
    assertThatThrownBy(() -> fileStorageConfig.initialize())
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("필수 설정인 'app.upload.path'가 누락되었습니다.");
  }

  @Test
  @DisplayName("Scenario: 성공 - 경로가 존재하지 않아도 경고 로그만 남기고 예외는 발생하지 않는다")
  void initialize_success_even_if_path_does_not_exist() {
    // Given
    Path nonExistentPath = tempDir.resolve("non-existent");
    ReflectionTestUtils.setField(fileStorageConfig, "uploadPath", nonExistentPath.toString());

    // When & Then
    assertThatCode(() -> fileStorageConfig.initialize())
        .doesNotThrowAnyException();
  }
}
