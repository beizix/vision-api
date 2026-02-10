package io.vision.api.useCases.auth.manageToken.application.domain.model;

/**
 * 토큰 갱신 요청 시 사용되는 리프레시 토큰 정보를 담은 커맨드 객체입니다.
 *
 * @param refreshToken 기존 리프레시 토큰 문자열
 */
public record RefreshTokenCmd(String refreshToken) {}
