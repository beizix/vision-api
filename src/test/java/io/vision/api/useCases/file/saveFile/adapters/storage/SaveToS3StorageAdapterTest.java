package io.vision.api.useCases.file.saveFile.adapters.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import io.awspring.cloud.s3.S3Template;
import io.vision.api.useCases.file.saveFile.application.domain.model.FileStorageType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class SaveToS3StorageAdapterTest {

  @InjectMocks
  private SaveToS3StorageAdapter saveToS3StorageAdapter;

  @Mock
  private S3Template s3Template;

  private final String bucketName = "test-bucket";
  private final String bucketFolder = "test-folder";

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(saveToS3StorageAdapter, "bucketName", bucketName);
    ReflectionTestUtils.setField(saveToS3StorageAdapter, "bucketFolder", bucketFolder);
  }

  @Test
  @DisplayName("Scenario: 성공 - 저장소 타입이 S3임을 반환한다")
  void getStorageType_success() {
    // When
    FileStorageType storageType = saveToS3StorageAdapter.getStorageType();

    // Then
    assertThat(storageType).isEqualTo(FileStorageType.S3);
  }

  @Test
  @DisplayName("Scenario: 성공 - S3Template을 사용하여 파일을 업로드한다")
  void operate_success() throws IOException {
    // Given
    byte[] content = "test content".getBytes();
    InputStream inputStream = new ByteArrayInputStream(content);
    String subPath = "2026/02/12";
    String filename = "test.txt";
    String expectedPath = bucketFolder + "/" + subPath + "/" + filename;

    // When
    saveToS3StorageAdapter.operate(inputStream, subPath, filename);

    // Then
    verify(s3Template).upload(eq(bucketName), eq(expectedPath), any(InputStream.class));
  }
}
