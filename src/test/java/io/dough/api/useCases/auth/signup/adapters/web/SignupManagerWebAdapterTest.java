package io.dough.api.useCases.auth.signup.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.common.application.enums.Role;
import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.dough.api.useCases.auth.signup.adapters.web.model.SignupManagerRequest;
import io.dough.api.useCases.auth.signup.application.SignupUseCase;
import io.dough.api.useCases.auth.signup.application.domain.model.SignupCmd;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(SignupManagerWebAdapter.class)
class SignupManagerWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private SignupUseCase signupUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 유효한 관리자 가입 요청시 200 OK를 반환한다")
  void signup_manager_success() throws Exception {
    // Given
    SignupManagerRequest req = new SignupManagerRequest("manager@dough.io", "password", "Manager User");
    AuthToken mockToken = new AuthToken("access-token", "refresh-token");
    when(signupUseCase.operate(any(SignupCmd.class))).thenReturn(mockToken);

    // When
    mockMvc
        .perform(post("/api/v1/manager/signup").content(json(req)).contentType("application/json"))
        .andExpect(status().isOk());

    // Then
    verify(signupUseCase)
        .operate(
            argThat(cmd -> cmd.email().equals("manager@dough.io") && cmd.role() == Role.MANAGER));
  }
}
