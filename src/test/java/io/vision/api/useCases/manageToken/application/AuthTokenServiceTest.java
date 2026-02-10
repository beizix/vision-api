package io.vision.api.useCases.manageToken.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.vision.api.common.application.enums.Role;
import io.vision.api.useCases.auth.manageToken.application.domain.ManageAuthTokenService;
import io.vision.api.useCases.auth.manageToken.application.domain.model.CreateTokenCmd;
import io.vision.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.vision.api.useCases.auth.manageToken.application.domain.model.RefreshTokenCmd;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import javax.crypto.SecretKey;

import io.vision.api.useCases.auth.manageToken.application.ports.RefreshTokenPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthTokenServiceTest {

  private ManageAuthTokenService authTokenService;
  private RefreshTokenPortOut refreshTokenPortOut;
  // 테스트용 키는 256비트(32자) 이상이어야 안전하게 HS256 알고리즘을 사용할 수 있습니다.
  private final String secret = "v-api-test-secret-key-must-be-long-enough-for-hs256";
  private final long accessValidity = 60000L; // 60초
  private final long refreshValidity = 120000L; // 120초
  private SecretKey secretKey;

  @BeforeEach
  void setUp() {
    refreshTokenPortOut = mock(RefreshTokenPortOut.class);
    authTokenService = new ManageAuthTokenService(secret, accessValidity, refreshValidity, refreshTokenPortOut);
    secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  @DisplayName("Scenario: 성공 - 토큰 생성 시 DB에 Refresh Token을 저장한다")
  void create_token_save_refresh_token() {
    // Given
    CreateTokenCmd cmd = new CreateTokenCmd("test@example.com", "Test User", Role.USER);

    // When
    AuthToken token = authTokenService.createToken(cmd);

    // Then
    verify(refreshTokenPortOut).save("test@example.com", token.refreshToken());
  }

  @Test
  @DisplayName("Scenario: 성공 - DB에 저장된 Refresh Token과 일치하면 토큰을 재발급한다")
  void refresh_token_success() throws InterruptedException {
    // Given
    String email = "manager@example.com";
    Role role = Role.MANAGER;
    CreateTokenCmd createCmd = new CreateTokenCmd(email, "Manager User", role);
    AuthToken originalToken = authTokenService.createToken(createCmd);

    when(refreshTokenPortOut.get(originalToken.refreshToken()))
        .thenReturn(Optional.of(new RefreshTokenPortOut.RefreshUser(email, "Manager User", role)));

    // Ensure tokens have different timestamps
    Thread.sleep(1001);

    // When
    AuthToken refreshedToken = authTokenService.refreshToken(new RefreshTokenCmd(originalToken.refreshToken()));

    // Then
    assertThat(refreshedToken.accessToken()).isNotEqualTo(originalToken.accessToken());
    assertThat(authTokenService.getRole(refreshedToken.accessToken())).isEqualTo("ROLE_MANAGER");
  }

  @Test
  @DisplayName("Scenario: 실패 - DB에 저장되지 않은 Refresh Token이면 예외가 발생한다")
  void refresh_token_fail_not_in_db() {
    // Given
    String token = "invalid-token";
    when(refreshTokenPortOut.get(anyString())).thenReturn(Optional.empty());

    // When & Then
    org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
      authTokenService.refreshToken(new RefreshTokenCmd(token));
    });
  }

  @Test
  @DisplayName("Scenario: 성공 - 유효한 토큰 검증 시 true를 반환한다")
  void validate_token_success() {
    // Given
    CreateTokenCmd cmd = new CreateTokenCmd("test@example.com", "Test User", Role.USER);
    AuthToken token = authTokenService.createToken(cmd);
    String accessToken = token.accessToken();

    // When
    boolean isValid = authTokenService.validateToken(accessToken);

    // Then
    assertThat(isValid).isTrue();
  }

  @Test
  @DisplayName("Scenario: 실패 - 조작된 토큰 검증 시 false를 반환한다")
  void validate_token_fail_invalid_signature() {
    // Given
    String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0In0.invalid_signature";

    // When
    boolean isValid = authTokenService.validateToken(invalidToken);

    // Then
    assertThat(isValid).isFalse();
  }

  @Test
  @DisplayName("Scenario: 성공 - 토큰에서 사용자 이메일(Subject) 추출")
  void get_subject_success() {
    // Given
    String email = "user@example.com";
    CreateTokenCmd cmd = new CreateTokenCmd(email, "User", Role.USER);
    AuthToken token = authTokenService.createToken(cmd);

    // When
    String subject = authTokenService.getSubject(token.accessToken());

    // Then
    assertThat(subject).isEqualTo(email);
  }

  @Test
  @DisplayName("Scenario: 성공 - 토큰에서 역할(Roles) 추출")
  void get_roles_success() {
    // Given
    String email = "manager@example.com";
    Role role = Role.MANAGER;
    CreateTokenCmd cmd = new CreateTokenCmd(email, "Manager User", role);

    // When
    AuthToken authToken = authTokenService.createToken(cmd);

    // Then
    String accessToken = authToken.accessToken();
    Claims claims = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(accessToken)
        .getPayload();

    assertThat(claims.getSubject()).isEqualTo(email);

    String extractedRole = claims.get("role", String.class);
    assertThat(extractedRole).isEqualTo("ROLE_MANAGER");
  }

  @Test
  @DisplayName("Scenario: 성공 - 토큰에서 표시 이름(DisplayName) 추출")
  void get_display_name_success() {
    // Given
    String email = "user@example.com";
    String displayName = "Super User";
    CreateTokenCmd cmd = new CreateTokenCmd(email, displayName, Role.USER);
    AuthToken token = authTokenService.createToken(cmd);

    // When
    String extractedDisplayName = authTokenService.getDisplayName(token.accessToken());

    // Then
    assertThat(extractedDisplayName).isEqualTo(displayName);
  }

  @Test
  @DisplayName("Scenario: 성공 - 토큰에서 권한(Privileges) 추출")
  void get_privileges_success() {
    // Given
    String email = "manager@example.com";
    Role role = Role.MANAGER;
    CreateTokenCmd cmd = new CreateTokenCmd(email, "Manager User", role);

    // When
    AuthToken authToken = authTokenService.createToken(cmd);

    // Then
    String accessToken = authToken.accessToken();
    Claims claims = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(accessToken)
        .getPayload();

    @SuppressWarnings("unchecked")
    List<String> extractedPrivileges = claims.get("privileges", List.class);
    assertThat(extractedPrivileges).contains("ACCESS_MANAGER_API");
  }
}
