package io.dough.api.useCases.user.getUser.application.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.common.application.enums.Role;
import io.dough.api.useCases.file.getFileURL.application.GetFileURLUseCase;
import io.dough.api.useCases.user.getUser.application.LoadUser;
import io.dough.api.useCases.user.getUser.application.domain.model.GetUserCmd;
import io.dough.api.useCases.user.getUser.application.domain.model.UserDetail;
import io.dough.api.useCases.user.getUser.application.domain.model.UserLoaded;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUserServiceTest {

  @Mock private LoadUser loadUser;

  @Mock private GetFileURLUseCase getFileURLUseCase;

  @InjectMocks private GetUserService getUserService;

  @Test
  @DisplayName("Scenario: 성공 - 프로필 이미지가 없는 사용자 정보를 조회하면 URL이 null인 정보를 반환한다")
  void get_user_without_profile_image() {
    // Given
    UUID userId = UUID.randomUUID();
    GetUserCmd cmd = new GetUserCmd(userId);
    java.time.LocalDateTime now = java.time.LocalDateTime.now();
    UserLoaded loadedUser =
        new UserLoaded(userId, "test@example.com", "Test User", Role.USER, now, null);

    given(loadUser.operate(userId)).willReturn(loadedUser);

    // When
    UserDetail result = getUserService.operate(cmd);

    // Then
    assertThat(result.id()).isEqualTo(userId);
    assertThat(result.profileImageUrl()).isNull();
    verify(loadUser).operate(userId);
  }

  @Test
  @DisplayName("Scenario: 성공 - 프로필 이미지가 있는 사용자 정보를 조회하면 변환된 URL을 포함한 정보를 반환한다")
  void get_user_with_profile_image() {
    // Given
    UUID userId = UUID.randomUUID();
    UUID imageId = UUID.randomUUID();
    String expectedUrl = "http://example.com/files/" + imageId;
    GetUserCmd cmd = new GetUserCmd(userId);
    java.time.LocalDateTime now = java.time.LocalDateTime.now();
    UserLoaded loadedUser =
        new UserLoaded(userId, "test@example.com", "Test User", Role.USER, now, imageId);

    given(loadUser.operate(userId)).willReturn(loadedUser);
    given(getFileURLUseCase.operate(imageId)).willReturn(expectedUrl);

    // When
    UserDetail result = getUserService.operate(cmd);

    // Then
    assertThat(result.id()).isEqualTo(userId);
    assertThat(result.profileImageUrl()).isEqualTo(expectedUrl);
    verify(loadUser).operate(userId);
    verify(getFileURLUseCase).operate(imageId);
  }
}
