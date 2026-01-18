package io.vision.api.config.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.vision.api.useCases.auth.application.JwtUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@WebMvcTest(controllers = SecurityConfigTest.TestController.class)
@Import({
  SecurityConfig.class,
  JwtAuthenticationFilter.class,
  SecurityConfigTest.TestController.class
})
class SecurityConfigTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private JwtUseCase jwtUseCase;

  @RestController
  static class TestController {
    @GetMapping("/api/v1/auth/test")
    public String publicEndpoint() {
      return "ok";
    }

    @GetMapping("/api/v1/users/test")
    public String protectedEndpoint() {
      return "ok";
    }
  }

  @Test
  @DisplayName("Scenario: 성공 - 인증 제외 경로(/api/v1/auth/**)는 토큰 없이 접근 가능하다")
  void access_public_endpoint_success() throws Exception {
    mockMvc.perform(get("/api/v1/auth/test")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Scenario: 실패 - 보호된 경로는 토큰 없이 접근 시 401/403 응답을 반환한다")
  void access_protected_endpoint_fail() throws Exception {
    mockMvc.perform(get("/api/v1/users/test")).andExpect(status().isForbidden());
  }
}
