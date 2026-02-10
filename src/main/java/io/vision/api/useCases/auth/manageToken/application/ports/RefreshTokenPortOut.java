package io.vision.api.useCases.auth.manageToken.application.ports;

import io.vision.api.common.application.enums.Role;
import java.util.Optional;

/**
 * 리프레시 토큰의 저장 및 조회를 담당하는 출력 포트입니다.
 */
public interface RefreshTokenPortOut {

  /**
   * 리프레시 토큰을 통해 갱신 대상 사용자 정보를 조회하기 위한 모델입니다.
   *
   * @param email       사용자 이메일
   * @param displayName 사용자 표시 이름
   * @param role        사용자 권한
   */
  record RefreshUser(String email, String displayName, Role role) {}

  /**
   * 리프레시 토큰과 연결된 사용자 정보를 조회합니다.
   *
   * @param refreshToken 리프레시 토큰
   * @return 유효한 토큰일 경우 사용자 정보, 그렇지 않으면 Optional.empty()
   */
  Optional<RefreshUser> get(String refreshToken);

  /**
   * 리프레시 토큰을 저장하거나 업데이트합니다.
   *
   * @param email        사용자 이메일
   * @param refreshToken 발급된 리프레시 토큰
   */
  void save(String email, String refreshToken);
}
