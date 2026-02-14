package io.dough.api.useCases.user.getUser.application.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.common.application.enums.Role;
import io.dough.api.useCases.user.getUser.application.LoadUser;
import io.dough.api.useCases.user.getUser.application.domain.model.GetUserCmd;
import io.dough.api.useCases.user.getUser.application.domain.model.UserDetail;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUserServiceTest {

  @Mock
  private LoadUser loadUser;

  @InjectMocks
  private GetUserService getUserService;

  @Test
  @DisplayName("Scenario: 성공 - 사용자 ID로 상세 정보를 조회하면 해당 사용자 정보를 반환한다")
  void get_user_success() {
    // Given
    UUID userId = UUID.randomUUID();
    GetUserCmd cmd = new GetUserCmd(userId);
    UserDetail expectedUser = new UserDetail(userId, "test@example.com", "Test User", Role.USER,
        java.time.LocalDateTime.now(), null);

    given(loadUser.operate(userId)).willReturn(expectedUser);

    // When
    UserDetail result = getUserService.operate(cmd);

    // Then
    assertThat(result).isEqualTo(expectedUser);
    verify(loadUser).operate(userId);
  }
}
