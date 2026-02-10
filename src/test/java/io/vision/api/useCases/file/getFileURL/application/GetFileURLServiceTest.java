package io.vision.api.useCases.file.getFileURL.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

import io.vision.api.useCases.file.getFileURL.application.domain.GetFileURLService;
import io.vision.api.useCases.file.saveFile.application.domain.model.FileStorageType;
import io.vision.api.useCases.file.saveFile.application.domain.model.FileUploadType;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetFileURLServiceTest {

  @Mock
  private GetFileMetadata getFileMetadata;

  @Mock
  private GetFileURL localUrlStrategy;

  private GetFileURLService getFileResourceService;

  @BeforeEach
  void setUp() {
    // 전략 Mock 설정
    lenient().when(localUrlStrategy.getStorageType()).thenReturn(FileStorageType.LOCAL);

    getFileResourceService = new GetFileURLService(
      getFileMetadata,
        Set.of(localUrlStrategy)
    );
  }

  @Test
  @DisplayName("Scenario: 성공 - 저장된 파일 정보로 URL을 생성하여 반환한다")
  void get_resource_url_success() {
    // Given
    UUID fileId = UUID.randomUUID();
    String path = "/images/202602";
    String filename = "uuid.png";
    FileUploadType fileType = FileUploadType.UPLOAD_IMG_TO_LOCAL; // LOCAL StorageType

    io.vision.api.useCases.file.getFileURL.application.domain.model.GetFileURL fileResource = new io.vision.api.useCases.file.getFileURL.application.domain.model.GetFileURL(fileType, path, filename);
    String expectedUrl = "/uploads/images/202602/uuid.png";

    given(getFileMetadata.operate(fileId)).willReturn(fileResource);
    given(localUrlStrategy.operate(path, filename)).willReturn(expectedUrl);

    // When
    String result = getFileResourceService.operate(fileId);

    // Then
    assertThat(result).isEqualTo(expectedUrl);
    verify(getFileMetadata).operate(fileId);
    verify(localUrlStrategy).operate(path, filename);
  }

  @Test
  @DisplayName("Scenario: 실패 - 지원하지 않는 스토리지 타입인 경우 예외 발생")
  void get_resource_url_fail_no_strategy() {
    // Given
    UUID fileId = UUID.randomUUID();
    // FileUploadType을 Mocking하거나, 전략이 없는 새로운 타입이 필요하지만
    // 여기서는 전략 목록을 비워서 테스트
    GetFileURLService noStrategyService = new GetFileURLService(
      getFileMetadata,
        Set.of() // Empty strategies
    );

    io.vision.api.useCases.file.getFileURL.application.domain.model.GetFileURL fileResource = new io.vision.api.useCases.file.getFileURL.application.domain.model.GetFileURL(FileUploadType.UPLOAD_IMG_TO_LOCAL, "/path", "file.png");
    given(getFileMetadata.operate(fileId)).willReturn(fileResource);

    // When & Then
    assertThatThrownBy(() -> noStrategyService.operate(fileId))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("No resource url strategy found");
  }

  @Test
  @DisplayName("Scenario: 실패 - 파일 정보를 찾을 수 없는 경우 예외 전파")
  void get_resource_url_fail_not_found() {
    // Given
    UUID fileId = UUID.randomUUID();
    given(getFileMetadata.operate(fileId)).willThrow(new NoSuchElementException("Not found"));

    // When & Then
    assertThatThrownBy(() -> getFileResourceService.operate(fileId))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("Not found");
  }
}
