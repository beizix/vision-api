package io.vision.api.useCases.auth.manageToken.application.domain.model;

import io.vision.api.common.application.enums.Role;

/**
 * 신규 토큰 생성에 필요한 정보를 담은 커맨드 객체입니다.
 *
 * @param email       사용자 이메일 (Subject)
 * @param displayName 사용자 표시 이름
 * @param role        사용자 권한
 */
public record CreateTokenCmd(String email, String displayName, Role role) {
}
