package io.dough.api.useCases.auth.login.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.auth.login.adapters.web.model.LoginRequest;
import io.dough.api.useCases.auth.login.application.LoginUseCase;
import io.dough.api.useCases.auth.login.application.domain.model.LoginCmd;
import io.dough.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(LoginUserWebAdapter.class)
class LoginUserWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private LoginUseCase loginUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 유효한 이메일/비번으로 로그인 요청 시 토큰이 반환된다")
  void login_success() throws Exception {
    // Given
    LoginRequest req = new LoginRequest("test@test.com", "password123");
    AuthToken token = new AuthToken("access_token_value", "refresh_token_value");

    given(loginUseCase.operate(any(LoginCmd.class))).willReturn(token);

    // When
    mockMvc
        .perform(
            post("/api/v1/auth/login/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value("access_token_value"))
        .andExpect(jsonPath("$.refreshToken").value("refresh_token_value"));

    // Then
    verify(loginUseCase).operate(any(LoginCmd.class));
  }
}
