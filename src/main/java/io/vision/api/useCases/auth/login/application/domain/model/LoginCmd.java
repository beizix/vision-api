package io.vision.api.useCases.auth.login.application.domain.model;

/**
 * 로그인 요청 정보를 담은 커맨드 객체입니다.
 *
 * @param email    로그인할 이메일 주소
 * @param password 비밀번호
 */
public record LoginCmd(String email, String password) {}
