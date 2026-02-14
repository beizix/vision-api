package io.dough.api.config.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.auth.manageToken.application.ManageAuthTokenUseCase;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

  private JwtAuthenticationFilter filter;

  @Mock private ManageAuthTokenUseCase manageAuthTokenUseCase;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private MockFilterChain filterChain;

  @BeforeEach
  void setUp() {
    filter = new JwtAuthenticationFilter(manageAuthTokenUseCase);
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
    String userUuid = "123e4567-e89b-12d3-a456-426614174000"; // Example UUID
    String role = "ROLE_USER";
    var privileges = java.util.List.of("ACCESS_MANAGER_API");

    request.addHeader("Authorization", "Bearer " + token);

    given(manageAuthTokenUseCase.validateToken(token)).willReturn(true);
    given(manageAuthTokenUseCase.getSubject(token)).willReturn(userUuid);
    given(manageAuthTokenUseCase.getRole(token)).willReturn(role);
    given(manageAuthTokenUseCase.getPrivileges(token)).willReturn(privileges);

    // When
    filter.doFilterInternal(request, response, filterChain);

    // Then
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    assertThat(authentication).isNotNull();
    assertThat(authentication.getName()).isEqualTo(userUuid);

    // 검증: JwtUseCase에서 반환된 권한이 실제로 적용되었는지 확인
    assertThat(authentication.getAuthorities())
        .extracting(GrantedAuthority::getAuthority)
        .containsExactlyInAnyOrder("ROLE_USER", "ACCESS_MANAGER_API");

    verify(manageAuthTokenUseCase).getRole(token);
    verify(manageAuthTokenUseCase).getPrivileges(token);
  }

  @Test
  @DisplayName("Scenario: 실패 - 토큰이 없는 경우 인증되지 않음")
  void authentication_fail_no_token() throws Exception {
    // When
    filter.doFilterInternal(request, response, filterChain);

    // Then
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    assertThat(authentication).isNull();
    verify(manageAuthTokenUseCase, never()).validateToken(any());
  }
}
