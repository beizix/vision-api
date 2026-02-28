package io.dough.api.useCases.user.profile.saveProfile.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.user.profile.saveProfile.adapters.web.model.SaveProfileRequest;
import io.dough.api.useCases.user.profile.saveProfile.application.SaveProfileUseCase;
import io.dough.api.useCases.user.profile.saveProfile.application.domain.model.SaveProfileCmd;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(SaveProfileWebAdapter.class)
class SaveProfileWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private SaveProfileUseCase saveProfileUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 사용자 프로필을 성공적으로 업데이트한다")
  void save_user_profile_success() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();
    SaveProfileRequest request = new SaveProfileRequest("new.email@example.com", "New Name");

    // When & Then
    mockMvc
        .perform(
            patch("/api/v1/user/profile")
                .principal(() -> userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(request)))
        .andDo(print())
        .andExpect(status().isOk());

    verify(saveProfileUseCase).operate(any(SaveProfileCmd.class));
  }
}
