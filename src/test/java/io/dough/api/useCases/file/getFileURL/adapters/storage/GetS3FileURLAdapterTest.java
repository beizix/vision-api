package io.dough.api.useCases.file.getFileURL.adapters.storage;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.useCases.file.saveFile.application.domain.model.FileStorageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class GetS3FileURLAdapterTest {

  private GetS3FileURLAdapter getS3FileURLAdapter;

  private final String cloudFrontDomain = "cdn.dough.io";
  private final String bucketFolder = "uploads";

  @BeforeEach
  void setUp() {
    getS3FileURLAdapter = new GetS3FileURLAdapter();
    ReflectionTestUtils.setField(getS3FileURLAdapter, "cloudFrontDomain", cloudFrontDomain);
    ReflectionTestUtils.setField(getS3FileURLAdapter, "bucketFolder", bucketFolder);
  }

  @Test
  @DisplayName("Scenario: 성공 - 저장소 타입이 S3임을 반환한다")
  void getStorageType_success() {
    // When
    FileStorageType storageType = getS3FileURLAdapter.getStorageType();

    // Then
    assertThat(storageType).isEqualTo(FileStorageType.S3);
  }

  @Test
  @DisplayName("Scenario: 성공 - S3(CloudFront) URL을 올바르게 생성한다")
  void operate_success() {
    // Given
    String path = "2026/02/12";
    String filename = "image.png";

    // When
    String result = getS3FileURLAdapter.operate(path, filename);

    // Then
    assertThat(result)
        .isEqualTo(
            "https://" + cloudFrontDomain + "/" + bucketFolder + "/" + path + "/" + filename);
  }
}
