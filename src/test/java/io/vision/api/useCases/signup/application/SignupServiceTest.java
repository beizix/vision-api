package io.vision.api.useCases.signup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.vision.api.common.application.enums.Role;
import io.vision.api.useCases.auth.application.JwtUseCase;
import io.vision.api.useCases.auth.application.model.AuthCmd;
import io.vision.api.useCases.auth.application.model.AuthToken;
import io.vision.api.useCases.signup.application.model.SignupCmd;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class SignupServiceTest {

  @InjectMocks private SignupService signupService;

  @Mock private SignupPortOut signupPortOut;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private JwtUseCase jwtUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 정상적인 회원가입 요청 시 사용자를 저장하고 토큰을 발급한다")
  void signup_success() {
    // Given
    SignupCmd cmd = new SignupCmd("test@vision.io", "rawPassword", "Test User", Role.ROLE_USER);

    given(signupPortOut.existsByEmailAndRole(cmd.email(), cmd.role())).willReturn(false);
    given(passwordEncoder.encode(cmd.password())).willReturn("encodedPassword");
    given(jwtUseCase.createToken(any(AuthCmd.class)))
        .willReturn(new AuthToken("access", "refresh"));

    // When
    AuthToken token = signupService.operate(cmd);

    // Then
    assertThat(token).isNotNull();
    assertThat(token.accessToken()).isEqualTo("access");

    verify(signupPortOut)
        .save(
            argThat(
                user ->
                    user.email().equals(cmd.email())
                        && user.password().equals("encodedPassword")
                        && user.displayName().equals(cmd.displayName())
                        && user.role().equals(cmd.role())));

    verify(jwtUseCase)
        .createToken(
            argThat(
                authCmd ->
                    authCmd.email().equals(cmd.email()) && authCmd.roles().contains(cmd.role())));
  }

  @Test
  @DisplayName("Scenario: 실패 - 이미 해당 권한으로 가입된 이메일로 가입 시도 시 예외가 발생한다")
  void signup_fail_duplicate_email() {
    // Given
    SignupCmd cmd = new SignupCmd("duplicate@vision.io", "password", "User", Role.ROLE_USER);
    given(signupPortOut.existsByEmailAndRole(cmd.email(), cmd.role())).willReturn(true);

    // When & Then
    assertThatThrownBy(() -> signupService.operate(cmd))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("이미 해당 권한으로 가입된 이메일입니다.");
  }
}
