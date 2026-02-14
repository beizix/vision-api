package io.dough.api.useCases.auth.signup.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.dough.api.useCases.auth.signup.adapters.web.model.SignupUserRequest;
import io.dough.api.useCases.auth.signup.application.SignupUseCase;
import io.dough.api.useCases.auth.signup.application.domain.model.SignupCmd;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(SignupUserWebAdapter.class)
class SignupUserWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private SignupUseCase signupUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 사용자 회원가입 요청 시 토큰을 반환한다")
  @WithMockUser
  void signup_user_success() throws Exception {
    // Given
    SignupUserRequest req = new SignupUserRequest("user@dough.io", "password", "User Nickname");
    given(signupUseCase.operate(any(SignupCmd.class)))
        .willReturn(new AuthToken("access", "refresh"));

    // When
    mockMvc
        .perform(
            post("/api/v1/signup/user")
                .with(csrf())
                .content(json(req))
                .contentType("application/json"))
        .andExpect(status().isOk());

    // Then
    verify(signupUseCase).operate(any(SignupCmd.class));
  }
}
