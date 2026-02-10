package io.vision.api.useCases.auth.manageToken.application.domain.model;

/**
 * 인증 성공 후 발급되는 토큰 세트입니다.
 *
 * @param accessToken  API 호출 시 인증 헤더에 포함할 엑세스 토큰
 * @param refreshToken 엑세스 토큰 만료 시 갱신을 위해 사용하는 리프레시 토큰
 */
public record AuthToken(String accessToken, String refreshToken) {
}
