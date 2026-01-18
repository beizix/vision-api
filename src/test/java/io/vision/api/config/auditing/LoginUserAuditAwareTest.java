package io.vision.api.config.auditing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class LoginUserAuditAwareTest {

  private final LoginUserAuditAware auditAware = new LoginUserAuditAware();

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("Scenario: 인증 정보가 없는 경우 SYSTEM을 반환한다")
  void return_system_when_authentication_is_null() {
    // Given
    SecurityContextHolder.clearContext();

    // When
    Optional<String> result = auditAware.getCurrentAuditor();

    // Then
    assertThat(result).isPresent().contains("SYSTEM");
  }

  @Test
  @DisplayName("Scenario: 인증되지 않은 사용자인 경우 SYSTEM을 반환한다")
  void return_system_when_not_authenticated() {
    // Given
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(false);
    setAuthentication(authentication);

    // When
    Optional<String> result = auditAware.getCurrentAuditor();

    // Then
    assertThat(result).isPresent().contains("SYSTEM");
  }

  @Test
  @DisplayName("Scenario: 익명 사용자(anonymousUser)인 경우 SYSTEM을 반환한다")
  void return_system_when_anonymous_user() {
    // Given
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getName()).thenReturn("anonymousUser");
    setAuthentication(authentication);

    // When
    Optional<String> result = auditAware.getCurrentAuditor();

    // Then
    assertThat(result).isPresent().contains("SYSTEM");
  }

  @Test
  @DisplayName("Scenario: 인증된 사용자인 경우 사용자 이름을 반환한다")
  void return_username_when_authenticated() {
    // Given
    String expectedUsername = "test-user";
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getName()).thenReturn(expectedUsername);
    setAuthentication(authentication);

    // When
    Optional<String> result = auditAware.getCurrentAuditor();

    // Then
    assertThat(result).isPresent().contains(expectedUsername);
  }

  private void setAuthentication(Authentication authentication) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
  }
}
