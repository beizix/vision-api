package io.vision.api.useCases.login.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.vision.api.common.application.enums.Role;
import io.vision.api.useCases.auth.application.JwtUseCase;
import io.vision.api.useCases.auth.application.model.CreateTokenCmd;
import io.vision.api.useCases.auth.application.model.AuthToken;
import io.vision.api.useCases.login.application.model.LoginCmd;
import io.vision.api.useCases.login.application.model.LoginUser;
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

  @InjectMocks
  private LoginService loginService;

  @Mock
  private LoginPortOut loginPortOut;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtUseCase jwtUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 유효한 자격 증명으로 로그인 성공")
  void operate_success() {
    // Given
    String email = "test@example.com";
    String password = "password";
    String encodedPassword = "encodedPassword";
    LoginCmd cmd = new LoginCmd(email, password);
    LoginUser user = new LoginUser(UUID.randomUUID(), email, encodedPassword, "Test User", Role.USER);

    given(loginPortOut.loadUser(email)).willReturn(Optional.of(user));
    given(passwordEncoder.matches(password, encodedPassword)).willReturn(true);
    given(jwtUseCase.createToken(any(CreateTokenCmd.class)))
        .willReturn(new AuthToken("access", "refresh"));

    // When
    AuthToken token = loginService.operate(cmd);

    // Then
    assertThat(token).isNotNull();
    verify(jwtUseCase).createToken(any(CreateTokenCmd.class));
  }
}
