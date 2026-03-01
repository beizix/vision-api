package io.dough.api.useCases.user.profile.updatePassword.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.user.profile.updatePassword.adapters.web.model.UpdatePasswordRequest;
import io.dough.api.useCases.user.profile.updatePassword.application.UpdatePasswordUseCase;
import io.dough.api.useCases.user.profile.updatePassword.application.domain.model.UpdatePasswordCmd;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(UpdatePasswordWebAdapter.class)
class UpdatePasswordWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private UpdatePasswordUseCase updatePasswordUseCase;

  private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";

  @Test
  @DisplayName("Scenario: 성공 - 사용자 패스워드 변경 요청 시 유스케이스가 호출된다")
  void update_password_user_success() throws Exception {
    // Given
    UpdatePasswordRequest request = new UpdatePasswordRequest("currentPass", "newPass", "newPass");
    UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(
        USER_ID, null, List.of(new SimpleGrantedAuthority("ACCESS_USER_API")));

    // When
    mockMvc.perform(patch("/api/v1/user/profile/password")
            .principal(principal)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json(request)))
        .andExpect(status().isOk());

    // Then
    verify(updatePasswordUseCase).operate(any(UpdatePasswordCmd.class));
  }

  @Test
  @DisplayName("Scenario: 성공 - 매니저 패스워드 변경 요청 시 유스케이스가 호출된다")
  void update_password_manager_success() throws Exception {
    // Given
    UpdatePasswordRequest request = new UpdatePasswordRequest("currentPass", "newPass", "newPass");
    UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(
        USER_ID, null, List.of(new SimpleGrantedAuthority("ACCESS_MANAGER_API")));

    // When
    mockMvc.perform(patch("/api/v1/manager/profile/password")
            .principal(principal)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json(request)))
        .andExpect(status().isOk());

    // Then
    verify(updatePasswordUseCase).operate(any(UpdatePasswordCmd.class));
  }
}
