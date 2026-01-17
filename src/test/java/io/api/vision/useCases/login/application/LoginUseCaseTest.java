package io.api.vision.useCases.login.application;

import io.api.vision.useCases.auth.application.JwtUseCase;
import io.api.vision.useCases.auth.application.model.AuthCmd;
import io.api.vision.useCases.auth.application.model.AuthToken;
import io.api.vision.useCases.login.application.model.LoginCmd;
import io.api.vision.useCases.login.application.model.LoginUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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
    @DisplayName("Scenario: 성공 - 유효한 자격 증명으로 로그인 시 토큰 발급")
    void login_success() {
        // Given
        String email = "test@test.com";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";
        
        LoginCmd cmd = new LoginCmd(email, rawPassword);
        LoginUser user = new LoginUser(UUID.randomUUID(), email, encodedPassword, "ROLE_USER");
        AuthToken expectedToken = new AuthToken("access", "refresh");

        given(loginPortOut.loadUser(email)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);
        given(jwtUseCase.createToken(any(AuthCmd.class))).willReturn(expectedToken);

        // When
        AuthToken result = loginService.operate(cmd);

        // Then
        assertThat(result).isEqualTo(expectedToken);
        verify(loginPortOut).loadUser(email);
        verify(passwordEncoder).matches(rawPassword, encodedPassword);
    }
}
