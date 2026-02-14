package io.dough.api.useCases.auth.manageToken.application;

import io.dough.api.common.application.enums.Role;
import java.util.Optional;
import java.util.UUID;

/** 리프레시 토큰의 저장 및 조회를 담당하는 출력 포트입니다. */
public interface RefreshAuthToken {

  /**
   * 리프레시 토큰을 통해 갱신 대상 사용자 정보를 조회하기 위한 모델입니다.
   *
   * @param uuid 사용자 식별자
   * @param email 사용자 이메일
   * @param displayName 사용자 표시 이름
   * @param role 사용자 권한
   */
  record RefreshUser(UUID uuid, String email, String displayName, Role role) {}

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
   * @param uuid 사용자 식별자
   * @param refreshToken 발급된 리프레시 토큰
   */
  void save(UUID uuid, String refreshToken);
}
