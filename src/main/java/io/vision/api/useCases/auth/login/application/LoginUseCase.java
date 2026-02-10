package io.vision.api.useCases.auth.login.application;

import io.vision.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.vision.api.useCases.auth.login.application.domain.model.LoginCmd;

/**
 * 사용자 로그인을 처리하고 인증 토큰을 발급하는 유스케이스입니다.
 */
public interface LoginUseCase {

  /**
   * 로그인 요청을 처리하여 인증 토큰을 생성합니다.
   *
   * @param cmd 로그인에 필요한 정보 (이메일, 비밀번호)
   * @return 인증 성공 시 발급되는 액세스 토큰과 리프레시 토큰
   */
  AuthToken operate(LoginCmd cmd);
}
