package io.vision.api.useCases.auth.login.application.domain.model;

import io.vision.api.common.application.enums.Role;
import java.util.UUID;

/**
 * 로그인 프로세스 내에서 사용되는 사용자 도메인 모델입니다.
 *
 * @param id          사용자 고유 식별자
 * @param email       사용자 이메일
 * @param password    사용자 비밀번호 (해시된 상태)
 * @param displayName 사용자 표시 이름
 * @param role        사용자 권한
 */
public record GetUser(UUID id, String email, String password, String displayName, Role role) {}
