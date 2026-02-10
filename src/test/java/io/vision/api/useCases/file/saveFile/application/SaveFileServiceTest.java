package io.vision.api.useCases.file.saveFile.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import io.vision.api.useCases.file.saveFile.application.domain.SaveFileService;
import io.vision.api.useCases.file.saveFile.application.domain.model.FileStorageType;
import io.vision.api.useCases.file.saveFile.application.domain.model.FileUploadType;
import io.vision.api.useCases.file.saveFile.application.domain.model.SaveFileMetadata;
import io.vision.api.useCases.file.saveFile.application.domain.model.SaveFileMetadataCmd;
import io.vision.api.useCases.file.saveFile.application.domain.model.SaveFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import io.vision.api.useCases.file.saveFile.application.ports.SaveFileMetadataPortOut;
import io.vision.api.useCases.file.saveFile.application.ports.SaveToFileStoragePortOut;
import org.apache.tika.Tika;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaveFileServiceTest {

  @Mock
  private SaveFileMetadataPortOut saveFileMetadataPortOut;

  @Mock
  private Tika tika;

  @Mock
  private SaveToFileStoragePortOut localStorageStrategy;

  private SaveFileService uploadFileService;

  @BeforeEach
  void setUp() {
    // 기본적으로 로컬 스토리지 전략을 지원하도록 설정
    // 일부 실패 테스트에서는 전략을 조회하지 않으므로 lenient() 사용
    lenient().when(localStorageStrategy.getStorageType()).thenReturn(FileStorageType.LOCAL);
    Set<SaveToFileStoragePortOut> strategies = Set.of(localStorageStrategy);

    uploadFileService = new SaveFileService(strategies, saveFileMetadataPortOut, tika);
  }

  @Test
  @DisplayName("Scenario: 성공 - 유효한 이미지 파일을 업로드한다")
  void upload_success() throws IOException {
    // Given
    InputStream inputStream = new ByteArrayInputStream("test image content".getBytes());
    String originalFilename = "test.png";
    long fileSize = 1024L;
    FileUploadType type = FileUploadType.UPLOAD_IMG_TO_LOCAL;
    UUID expectedId = UUID.randomUUID();

    given(tika.detect(any(InputStream.class), eq(originalFilename))).willReturn("image/png");
    given(saveFileMetadataPortOut.operate(any(SaveFileMetadataCmd.class)))
        .willReturn(Optional.of(new SaveFileMetadata(expectedId, type, "/path", "uuid.png", originalFilename, fileSize)));

    // When
    Optional<SaveFile> result = uploadFileService.operate(type, inputStream, originalFilename, fileSize);

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().id()).isEqualTo(expectedId);
    assertThat(result.get().originName()).isEqualTo(originalFilename);

    then(localStorageStrategy).should().operate(any(InputStream.class), anyString(), anyString());
    then(saveFileMetadataPortOut).should().operate(any(SaveFileMetadataCmd.class));
  }

  @Test
  @DisplayName("Scenario: 실패 - 파일 확장자가 없는 경우 예외 발생")
  void upload_fail_no_extension() {
    // Given
    InputStream inputStream = new ByteArrayInputStream("content".getBytes());
    String originalFilename = "testfile"; // No extension

    // When & Then
    assertThatThrownBy(() ->
        uploadFileService.operate(FileUploadType.UPLOAD_IMG_TO_LOCAL, inputStream, originalFilename, 100L))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("파일 확장자가 존재하지 않습니다");
  }

  @Test
  @DisplayName("Scenario: 실패 - 허용되지 않는 파일 확장자인 경우 예외 발생")
  void upload_fail_invalid_extension() {
    // Given
    InputStream inputStream = new ByteArrayInputStream("content".getBytes());
    String originalFilename = "test.exe"; // Not allowed for USER_IMAGE

    // When & Then
    assertThatThrownBy(() ->
        uploadFileService.operate(FileUploadType.UPLOAD_IMG_TO_LOCAL, inputStream, originalFilename, 100L))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("허용되지 않는 파일 확장자입니다");
  }

  @Test
  @DisplayName("Scenario: 실패 - 확장자와 일치하지 않는 MIME Type 인 경우 예외 발생")
  void upload_fail_mismatch_mimetype() throws IOException {
    // Given
    InputStream inputStream = new ByteArrayInputStream("fake image".getBytes());
    String originalFilename = "test.png";

    // Tika가 실행 파일로 감지
    given(tika.detect(any(InputStream.class), eq(originalFilename))).willReturn("application/x-dosexec");

    // When & Then
    assertThatThrownBy(() ->
        uploadFileService.operate(FileUploadType.UPLOAD_IMG_TO_LOCAL, inputStream, originalFilename, 100L))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("허용되지 않는 MIME Type 입니다");
  }

  @Test
  @DisplayName("Scenario: 실패 - 지원하지 않는 스토리지 타입인 경우 예외 발생")
  void upload_fail_no_strategy() throws IOException {
    // Given: 지원하는 전략이 비어있는 서비스 생성
    SaveFileService noStrategyService = new SaveFileService(Set.of(), saveFileMetadataPortOut, tika);

    InputStream inputStream = new ByteArrayInputStream("content".getBytes());
    String originalFilename = "test.png";

    given(tika.detect(any(InputStream.class), eq(originalFilename))).willReturn("image/png");

    // When & Then
    assertThatThrownBy(() ->
        noStrategyService.operate(FileUploadType.UPLOAD_IMG_TO_LOCAL, inputStream, originalFilename, 100L))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("No file upload strategy found");
  }

  @Test
  @DisplayName("Scenario: 예외 - 입력값이 유효하지 않은 경우 Empty 반환")
  void upload_fail_invalid_input() {
    // Given
    InputStream inputStream = new ByteArrayInputStream("content".getBytes());

    // When & Then
    assertThat(uploadFileService.operate(FileUploadType.UPLOAD_IMG_TO_LOCAL, null, "test.png", 100L))
        .isEmpty();
    assertThat(uploadFileService.operate(FileUploadType.UPLOAD_IMG_TO_LOCAL, inputStream, null, 100L))
        .isEmpty();
    assertThat(uploadFileService.operate(FileUploadType.UPLOAD_IMG_TO_LOCAL, inputStream, "", 100L))
        .isEmpty();
  }
}
