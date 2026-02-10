package io.vision.api.useCases.login.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.vision.api.common.application.enums.Role;
import io.vision.api.useCases.auth.manageToken.application.ManageAuthTokenUseCase;
import io.vision.api.useCases.auth.manageToken.application.domain.model.CreateTokenCmd;
import io.vision.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.vision.api.useCases.auth.login.application.domain.LoginService;
import io.vision.api.useCases.auth.login.application.domain.model.LoginCmd;
import io.vision.api.useCases.auth.login.application.domain.model.GetUser;
import java.util.Optional;
import java.util.UUID;

import io.vision.api.useCases.auth.login.application.ports.GetUserPortOut;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

  @InjectMocks
  private LoginService loginService;

  @Mock
  private GetUserPortOut getUserPortOut;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private ManageAuthTokenUseCase manageAuthTokenUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 유효한 자격 증명으로 로그인 성공")
  void operate_success() {
    // Given
    String email = "test@example.com";
    String password = "password";
    String encodedPassword = "encodedPassword";
    LoginCmd cmd = new LoginCmd(email, password);
    GetUser user = new GetUser(UUID.randomUUID(), email, encodedPassword, "Test User", Role.USER);

    given(getUserPortOut.operate(email)).willReturn(Optional.of(user));
    given(passwordEncoder.matches(password, encodedPassword)).willReturn(true);
    given(manageAuthTokenUseCase.createToken(any(CreateTokenCmd.class)))
        .willReturn(new AuthToken("access", "refresh"));

    // When
    AuthToken token = loginService.operate(cmd);

    // Then
    assertThat(token).isNotNull();
    verify(manageAuthTokenUseCase).createToken(any(CreateTokenCmd.class));
  }
}
