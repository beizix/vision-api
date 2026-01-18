package io.vision.api.config.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import io.vision.api.common.application.enums.Role;
import io.vision.api.useCases.auth.application.JwtUseCase;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

  private JwtAuthenticationFilter filter;

  @Mock private JwtUseCase jwtUseCase;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private MockFilterChain filterChain;

  @BeforeEach
  void setUp() {
    filter = new JwtAuthenticationFilter(jwtUseCase);
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    filterChain = new MockFilterChain();
    SecurityContextHolder.clearContext();
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("Scenario: 성공 - 유효한 토큰으로 인증 성공 및 권한 부여")
  void authentication_success() throws Exception {
    // Given
    String token = "valid-token";
    String email = "test@example.com";
    List<String> roles = List.of(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name());

    request.addHeader("Authorization", "Bearer " + token);

    given(jwtUseCase.validateToken(token)).willReturn(true);
    given(jwtUseCase.getSubject(token)).willReturn(email);
    given(jwtUseCase.getRoles(token)).willReturn(roles);

    // When
    filter.doFilterInternal(request, response, filterChain);

    // Then
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    assertThat(authentication).isNotNull();
    assertThat(authentication.getName()).isEqualTo(email);

    // 검증: JwtUseCase에서 반환된 역할이 실제로 적용되었는지 확인
    assertThat(authentication.getAuthorities())
        .extracting("authority")
        .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");

    verify(jwtUseCase).getRoles(token);
  }

  @Test
  @DisplayName("Scenario: 실패 - 토큰이 없는 경우 인증되지 않음")
  void authentication_fail_no_token() throws Exception {
    // When
    filter.doFilterInternal(request, response, filterChain);

    // Then
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    assertThat(authentication).isNull();
    verify(jwtUseCase, never()).validateToken(any());
  }
}
