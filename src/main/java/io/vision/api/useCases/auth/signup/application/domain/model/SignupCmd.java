package io.vision.api.useCases.auth.signup.application.domain.model;

import io.vision.api.common.application.enums.Role;

/**
 * 회원가입에 필요한 사용자 정보를 담은 커맨드 객체입니다.
 *
 * @param email       사용자 이메일 (로그인 ID로 사용)
 * @param password    비밀번호 (암호화되어 저장됨)
 * @param displayName 서비스에서 표시될 사용자 이름
 * @param role        부여할 권한 (USER, MANAGER 등)
 */
public record SignupCmd(String email, String password, String displayName, Role role) {
}
