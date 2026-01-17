package io.api.vision.useCases.auth.application;

import io.api.vision.useCases.auth.application.model.AuthCmd;
import io.api.vision.useCases.auth.application.model.AuthToken;
import io.api.vision.useCases.auth.application.model.RefreshTokenCmd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;
    // 테스트용 키는 256비트(32자) 이상이어야 안전하게 HS256 알고리즘을 사용할 수 있습니다.
    private final String secret = "v-api-test-secret-key-must-be-long-enough-for-hs256"; 
    private final long accessValidity = 60000L; // 60초
    private final long refreshValidity = 120000L; // 120초

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(secret, accessValidity, refreshValidity);
    }

    @Test
    @DisplayName("Scenario: 성공 - 유효한 토큰 검증 시 true를 반환한다")
    void validate_token_success() {
        // Given
        AuthCmd cmd = new AuthCmd("test@example.com");
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
        AuthCmd cmd = new AuthCmd(email);
        AuthToken token = jwtService.createToken(cmd);

        // When
        String subject = jwtService.getSubject(token.accessToken());

        // Then
        assertThat(subject).isEqualTo(email);
    }
}
