package io.vision.api.useCases.auth.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.vision.api.support.WebMvcTestBase;
import io.vision.api.useCases.auth.adapters.web.model.RefreshReq;
import io.vision.api.useCases.auth.adapters.web.model.ValidateReq;
import io.vision.api.useCases.auth.application.model.AuthToken;
import io.vision.api.useCases.auth.application.model.RefreshTokenCmd;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;

@WebMvcTest(AuthWebAdapter.class)
class AuthWebAdapterTest extends WebMvcTestBase {

  @Test
  @DisplayName("Scenario: 성공 - 유효한 리프레시 토큰으로 토큰 재발급")
  void refresh_token_success() throws Exception {
    // Given
    RefreshReq req = new RefreshReq("valid_refresh_token");
    AuthToken token = new AuthToken("new_access_token", "new_refresh_token");

    given(authTokenUseCase.refreshToken(any(RefreshTokenCmd.class))).willReturn(token);

    // When
    mockMvc
        .perform(
            post("/api/v1/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(json(req)))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value("new_access_token"))
        .andExpect(jsonPath("$.refreshToken").value("new_refresh_token"));
  }

  @Test
  @DisplayName("Scenario: 성공 - 토큰 유효성 검증")
  void validate_token_success() throws Exception {
    // Given
    ValidateReq req = new ValidateReq("valid_access_token");
    given(authTokenUseCase.validateToken(req.token())).willReturn(true);

    // When
    mockMvc
        .perform(
            post("/api/v1/auth/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(req)))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.valid").value(true));
  }
}
