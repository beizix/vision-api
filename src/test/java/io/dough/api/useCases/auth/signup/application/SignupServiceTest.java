package io.dough.api.useCases.auth.signup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.common.application.enums.Role;
import io.dough.api.common.application.utils.MessageUtils;
import io.dough.api.useCases.auth.manageToken.application.ManageAuthTokenUseCase;
import io.dough.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.dough.api.useCases.auth.manageToken.application.domain.model.CreateTokenCmd;
import io.dough.api.useCases.auth.signup.application.domain.SignupService;
import io.dough.api.useCases.auth.signup.application.domain.model.SignupCmd;
import io.dough.api.useCases.auth.signup.application.domain.model.SignupUser;
import java.util.UUID;
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

  @Mock private ManageSignup manageSignup;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private ManageAuthTokenUseCase manageAuthTokenUseCase;

  @Mock private MessageUtils messageUtils;

  @Test
  @DisplayName("Scenario: 성공 - 정상적인 회원가입 요청 시 사용자를 저장하고 토큰을 발급한다")
  void signup_success() {
    // Given
    SignupCmd cmd = new SignupCmd("test@dough.io", "rawPassword", "Test User", Role.USER);

    given(manageSignup.existsByEmailAndRole(cmd.email(), cmd.role())).willReturn(false);
    given(passwordEncoder.encode(cmd.password())).willReturn("encodedPassword");
    given(manageSignup.save(any(SignupUser.class)))
        .willAnswer(
            invocation -> {
              SignupUser user = invocation.getArgument(0);
              return new SignupUser(
                  UUID.randomUUID(),
                  user.email(),
                  user.password(),
                  user.displayName(),
                  user.role());
            });
    given(manageAuthTokenUseCase.createToken(any(CreateTokenCmd.class)))
        .willReturn(new AuthToken("access", "refresh"));

    // When
    AuthToken token = signupService.operate(cmd);

    // Then
    assertThat(token).isNotNull();
    assertThat(token.accessToken()).isEqualTo("access");

    verify(manageSignup)
        .save(
            argThat(
                user ->
                    user.email().equals(cmd.email())
                        && user.password().equals("encodedPassword")
                        && user.displayName().equals(cmd.displayName())
                        && user.role().equals(cmd.role())));

    verify(manageAuthTokenUseCase)
        .createToken(
            argThat(
                authCmd -> authCmd.email().equals(cmd.email()) && authCmd.role() == cmd.role()));
  }

  @Test
  @DisplayName("Scenario: 실패 - 이미 해당 권한으로 가입된 이메일로 가입 시도 시 예외가 발생한다")
  void signup_fail_duplicate_email() {
    // Given
    SignupCmd cmd = new SignupCmd("duplicate@dough.io", "password", "User", Role.USER);
    given(manageSignup.existsByEmailAndRole(cmd.email(), cmd.role())).willReturn(true);
    String errorMessage = "이미 해당 권한으로 가입된 이메일입니다.";
    given(messageUtils.getMessage("exception.auth.email_already_exists")).willReturn(errorMessage);

    // When & Then
    assertThatThrownBy(() -> signupService.operate(cmd))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errorMessage);
  }
}
