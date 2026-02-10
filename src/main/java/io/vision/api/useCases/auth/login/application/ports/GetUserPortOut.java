package io.vision.api.useCases.auth.login.application.ports;

import io.vision.api.useCases.auth.login.application.domain.model.GetUser;
import java.util.Optional;

/**
 * 로그인 유스케이스에서 요구하는 사용자 정보 조회를 정의한 출력 포트입니다.
 */
public interface GetUserPortOut {

  /**
   * 이메일을 통해 로그인 사용자 정보를 조회합니다.
   *
   * @param email 조회할 사용자 이메일
   * @return 사용자 정보가 존재할 경우 LoginUser, 그렇지 않을 경우 Optional.empty()
   */
  Optional<GetUser> operate(String email);
}
