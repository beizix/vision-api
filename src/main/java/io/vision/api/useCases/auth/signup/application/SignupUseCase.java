package io.vision.api.useCases.auth.signup.application;

import io.vision.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.vision.api.useCases.auth.signup.application.domain.model.SignupCmd;

/**
 * 새로운 사용자를 시스템에 등록하고(회원가입), 인증 토큰을 발급합니다.
 */
public interface SignupUseCase {

  /**
   * 회원가입 요청을 처리합니다.
   *
   * @param cmd 회원가입에 필요한 정보 (이메일, 비밀번호 등)
   * @return 회원가입 완료 후 즉시 로그인 처리를 위한 인증 토큰
   */
  AuthToken operate(SignupCmd cmd);
}
