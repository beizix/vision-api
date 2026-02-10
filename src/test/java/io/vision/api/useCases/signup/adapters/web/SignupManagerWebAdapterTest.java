package io.vision.api.useCases.signup.adapters.web;

import io.vision.api.common.application.enums.Role;
import io.vision.api.support.WebMvcTestBase;
import io.vision.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.vision.api.useCases.auth.signup.adapters.web.SignupManagerWebAdapter;
import io.vision.api.useCases.auth.signup.adapters.web.model.SignupManagerReq;
import io.vision.api.useCases.auth.signup.application.SignupUseCase;
import io.vision.api.useCases.auth.signup.application.domain.model.SignupCmd;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignupManagerWebAdapter.class)
class SignupManagerWebAdapterTest extends WebMvcTestBase {

  @MockitoBean
  private SignupUseCase signupUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 유효한 관리자 가입 요청시 200 OK를 반환한다")
  void signup_manager_success() throws Exception {
    // Given
    SignupManagerReq req = new SignupManagerReq("manager@vision.io", "password", "Manager User");
    AuthToken mockToken = new AuthToken("access-token", "refresh-token");
    when(signupUseCase.operate(any(SignupCmd.class))).thenReturn(mockToken);

    // When
    mockMvc.perform(post("/api/v1/manager/signup")
        .content(json(req))
        .contentType("application/json"))
        .andExpect(status().isOk());

    // Then
    verify(signupUseCase).operate(argThat(cmd -> cmd.email().equals("manager@vision.io") &&
        cmd.role() == Role.MANAGER));
  }
}
