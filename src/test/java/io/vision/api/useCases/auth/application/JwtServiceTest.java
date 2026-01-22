package io.vision.api.useCases.auth.application;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.vision.api.common.application.enums.Role;
import io.vision.api.useCases.auth.application.model.CreateTokenCmd;
import io.vision.api.useCases.auth.application.model.AuthToken;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

  private JwtService jwtService;
  // 테스트용 키는 256비트(32자) 이상이어야 안전하게 HS256 알고리즘을 사용할 수 있습니다.
  private final String secret = "v-api-test-secret-key-must-be-long-enough-for-hs256";
  private final long accessValidity = 60000L; // 60초
  private final long refreshValidity = 120000L; // 120초
  private SecretKey secretKey;

  @BeforeEach
  void setUp() {
    jwtService = new JwtService(secret, accessValidity, refreshValidity);
    secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  @DisplayName("Scenario: 성공 - 유효한 토큰 검증 시 true를 반환한다")
  void validate_token_success() {
    // Given
    CreateTokenCmd cmd = new CreateTokenCmd("test@example.com", "Test User", List.of(Role.ROLE_USER));
    AuthToken token = jwtService.createToken(cmd);
    String accessToken = token.accessToken();

    // When
    boolean isValid = jwtService.validateToken(accessToken);

    // Then
    assertThat(isValid).isTrue();
  }

  @Test
  @DisplayName("Scenario: 실패 - 조작된 토큰 검증 시 false를 반환한다")
  void validate_token_fail_invalid_signature() {
    // Given
    String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0In0.invalid_signature";

    // When
    boolean isValid = jwtService.validateToken(invalidToken);

    // Then
    assertThat(isValid).isFalse();
  }

  @Test
  @DisplayName("Scenario: 성공 - 토큰에서 사용자 이메일(Subject) 추출")
  void get_subject_success() {
    // Given
    String email = "user@example.com";
    CreateTokenCmd cmd = new CreateTokenCmd(email, "User", List.of(Role.ROLE_USER));
    AuthToken token = jwtService.createToken(cmd);

    // When
    String subject = jwtService.getSubject(token.accessToken());

    // Then
    assertThat(subject).isEqualTo(email);
  }

  @Test
  @DisplayName("Scenario: 성공 - 토큰에서 역할(Roles) 추출")
  void get_roles_success() {
    // Given
    String email = "manager@example.com";
    List<Role> roles = List.of(Role.ROLE_MANAGER, Role.ROLE_USER);
    CreateTokenCmd cmd = new CreateTokenCmd(email, "Manager User", roles);

    // When
    AuthToken authToken = jwtService.createToken(cmd);

    // Then
    String accessToken = authToken.accessToken();
    Claims claims = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(accessToken)
        .getPayload();

    assertThat(claims.getSubject()).isEqualTo(email);
    
    @SuppressWarnings("unchecked")
    List<String> extractedRoles = claims.get("roles", List.class);
    assertThat(extractedRoles).containsExactlyInAnyOrder("ROLE_MANAGER", "ROLE_USER");
  }

  @Test
  @DisplayName("Scenario: 성공 - 토큰에서 표시 이름(DisplayName) 추출")
  void get_display_name_success() {
    // Given
    String email = "user@example.com";
    String displayName = "Super User";
    CreateTokenCmd cmd = new CreateTokenCmd(email, displayName, List.of(Role.ROLE_USER));
    AuthToken token = jwtService.createToken(cmd);

    // When
    String extractedDisplayName = jwtService.getDisplayName(token.accessToken());

    // Then
    assertThat(extractedDisplayName).isEqualTo(displayName);
  }
}