package io.vision.api.config.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.vision.api.support.SecurityConfigTestWebAdapter;
import io.vision.api.useCases.auth.manageToken.application.ManageAuthTokenUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = SecurityConfigTestWebAdapter.class)
@Import({
    SecurityConfig.class,
    JwtAuthenticationFilter.class
})
class SecurityConfigTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ManageAuthTokenUseCase manageAuthTokenUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 인증 제외 경로(/api/v1/auth/**)는 토큰 없이 접근 가능하다")
  void access_public_endpoint_success() throws Exception {
    mockMvc.perform(get("/api/v1/auth/test")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Scenario: 실패 - 보호된 경로는 토큰 없이 접근 시 403 응답을 반환한다")
  void access_protected_endpoint_fail() throws Exception {
    mockMvc.perform(get("/api/v1/user/test")).andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("Scenario: 성공 - ACCESS_USER_API 권한이 있으면 유저 엔드포인트에 접근 가능하다")
  void access_user_endpoint_with_user_authority_success() throws Exception {
    mockMvc.perform(get("/api/v1/user/test")
            .with(user("user").authorities(new SimpleGrantedAuthority("ACCESS_USER_API"))))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Scenario: 실패 - ACCESS_USER_API 권한만으로는 관리자 엔드포인트에 접근할 수 없다")
  void access_manager_endpoint_with_user_authority_fail() throws Exception {
    mockMvc.perform(get("/api/v1/manager/test")
            .with(user("user").authorities(new SimpleGrantedAuthority("ACCESS_USER_API"))))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("Scenario: 성공 - ACCESS_MANAGER_API 권한이 있으면 관리자 엔드포인트에 접근 가능하다")
  void access_manager_endpoint_with_manager_authority_success() throws Exception {
    mockMvc.perform(get("/api/v1/manager/test")
            .with(user("manager").authorities(new SimpleGrantedAuthority("ACCESS_MANAGER_API"))))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Scenario: 실패 - ACCESS_MANAGER_API 권한만으로는 유저 엔드포인트에 접근할 수 없다")
  void access_user_endpoint_with_manager_authority_fail() throws Exception {
    mockMvc.perform(get("/api/v1/user/test")
            .with(user("manager").authorities(new SimpleGrantedAuthority("ACCESS_MANAGER_API"))))
        .andExpect(status().isForbidden());
  }
}
