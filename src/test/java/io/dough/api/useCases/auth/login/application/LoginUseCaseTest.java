package io.dough.api.useCases.auth.login.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.common.application.enums.Role;
import io.dough.api.common.application.utils.MessageUtils;
import io.dough.api.useCases.auth.login.application.domain.LoginService;
import io.dough.api.useCases.auth.login.application.domain.model.GetUserResult;
import io.dough.api.useCases.auth.login.application.domain.model.LoginCmd;
import io.dough.api.useCases.auth.manageToken.application.ManageAuthTokenUseCase;
import io.dough.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.dough.api.useCases.auth.manageToken.application.domain.model.CreateTokenCmd;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

  @InjectMocks private LoginService loginService;

  @Mock private GetUser getUser;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private ManageAuthTokenUseCase manageAuthTokenUseCase;

  @Mock private MessageUtils messageUtils;

  @Test
  @DisplayName("Scenario: 성공 - 유효한 자격 증명으로 로그인 성공")
  void operate_success() {
    // Given
    String email = "test@example.com";
    String password = "password";
    String encodedPassword = "encodedPassword";
    Role role = Role.USER;
    LoginCmd cmd = new LoginCmd(email, password, role);
    GetUserResult user =
        new GetUserResult(UUID.randomUUID(), email, encodedPassword, "Test User", role);

    given(getUser.operate(email, role)).willReturn(Optional.of(user));
    given(passwordEncoder.matches(password, encodedPassword)).willReturn(true);
    given(manageAuthTokenUseCase.createToken(any(CreateTokenCmd.class)))
        .willReturn(new AuthToken("access", "refresh"));

    // When
    AuthToken token = loginService.operate(cmd);

    // Then
    assertThat(token).isNotNull();
    verify(manageAuthTokenUseCase).createToken(any(CreateTokenCmd.class));
  }

  @Test
  @DisplayName("Scenario: 실패 - 존재하지 않는 사용자로 로그인 시도 시 예외가 발생한다")
  void operate_fail_user_not_found() {
    // Given
    String email = "notfound@example.com";
    Role role = Role.USER;
    LoginCmd cmd = new LoginCmd(email, "password", role);
    given(getUser.operate(email, role)).willReturn(Optional.empty());
    given(messageUtils.getMessage("exception.user.not_found")).willReturn("User not found");

    // When & Then
    org.assertj.core.api.Assertions.assertThatThrownBy(() -> loginService.operate(cmd))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("User not found");
  }

  @Test
  @DisplayName("Scenario: 실패 - 잘못된 비밀번호로 로그인 시도 시 예외가 발생한다")
  void operate_fail_invalid_password() {
    // Given
    String email = "test@example.com";
    String password = "wrongPassword";
    Role role = Role.USER;
    LoginCmd cmd = new LoginCmd(email, password, role);
    GetUserResult user =
        new GetUserResult(UUID.randomUUID(), email, "encodedPassword", "Name", role);

    given(getUser.operate(email, role)).willReturn(Optional.of(user));
    given(passwordEncoder.matches(password, user.password())).willReturn(false);
    given(messageUtils.getMessage("exception.auth.invalid_password"))
        .willReturn("Invalid password");

    // When & Then
    org.assertj.core.api.Assertions.assertThatThrownBy(() -> loginService.operate(cmd))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid password");
  }
}
