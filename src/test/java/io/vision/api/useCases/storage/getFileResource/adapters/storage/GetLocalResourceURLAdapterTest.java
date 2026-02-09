package io.vision.api.useCases.storage.getFileResource.adapters.storage;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GetLocalResourceURLAdapterTest {

  private final GetLocalResourceURLAdapter adapter = new GetLocalResourceURLAdapter();

  @Test
  @DisplayName("Scenario: 성공 - 경로와 파일명을 결합하여 올바른 URL을 생성한다")
  void operate_success() {
    // Given
    String path = "/images/user/";
    String filename = "profile.png";

    // When
    String result = adapter.operate(path, filename);

    // Then
    assertThat(result).isEqualTo("/uploads/images/user/profile.png");
  }

  @Test
  @DisplayName("Scenario: 성공 - 경로가 null이면 빈 문자열을 반환한다")
  void operate_return_empty_on_null() {
    assertThat(adapter.operate(null, "file.txt")).isEmpty();
    assertThat(adapter.operate("path", null)).isEmpty();
  }
}
