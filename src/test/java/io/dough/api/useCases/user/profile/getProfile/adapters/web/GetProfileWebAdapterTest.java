package io.dough.api.useCases.user.profile.getProfile.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.user.profile.getProfile.adapters.web.GetProfileWebAdapter;
import io.dough.api.useCases.user.profile.getProfile.application.GetProfileUseCase;
import io.dough.api.useCases.user.profile.getProfile.application.domain.model.GetProfileCmd;
import io.dough.api.useCases.user.profile.getProfile.application.domain.model.Profile;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(GetProfileWebAdapter.class)
class GetProfileWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private GetProfileUseCase getProfileUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 로그인된 사용자의 상세 정보를 반환한다")
  void get_my_profile_success() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();
    String email = "test@example.com";
    Profile expectedUser =
        new Profile(
            userId,
            email,
            "Test User",
            LocalDateTime.now(),
            null, // profileImageId
            "http://example.com/profile.png");

    given(getProfileUseCase.operate(any(GetProfileCmd.class))).willReturn(expectedUser);

    // When & Then
    mockMvc
        .perform(
            get("/api/v1/user/profile")
                .principal(() -> userId.toString())) // Mocking ID as Principal
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.email").value(email))
        .andExpect(jsonPath("$.displayName").value("Test User"))
        .andExpect(jsonPath("$.profileImageUrl").value("http://example.com/profile.png"))
        .andExpect(jsonPath("$.createdAt").exists());

    verify(getProfileUseCase).operate(any(GetProfileCmd.class));
  }
}
