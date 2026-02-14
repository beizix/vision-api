package io.dough.api.useCases.user.getUser.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.common.application.enums.Role;
import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.user.getUser.application.GetUserUseCase;
import io.dough.api.useCases.user.getUser.application.domain.model.GetUserCmd;
import io.dough.api.useCases.user.getUser.application.domain.model.UserDetail;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(GetUserWebAdapter.class)
class GetUserWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private GetUserUseCase getUserUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 로그인된 사용자의 상세 정보를 반환한다")
  void get_user_success() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();
    String email = "test@example.com";
    UserDetail expectedUser =
        new UserDetail(
            userId,
            email,
            "Test User",
            Role.USER,
            LocalDateTime.now(),
            "http://example.com/profile.png");

    given(getUserUseCase.operate(any(GetUserCmd.class))).willReturn(expectedUser);

    // When & Then
    mockMvc
        .perform(
            get("/api/v1/user/info").principal(() -> userId.toString())) // Mocking ID as Principal
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.email").value(email))
        .andExpect(jsonPath("$.displayName").value("Test User"))
        .andExpect(jsonPath("$.role").value("USER"))
        .andExpect(jsonPath("$.profileImageUrl").value("http://example.com/profile.png"))
        .andExpect(jsonPath("$.createdAt").exists());

    verify(getUserUseCase).operate(any(GetUserCmd.class));
  }
}
