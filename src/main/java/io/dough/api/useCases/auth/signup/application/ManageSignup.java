package io.dough.api.useCases.auth.signup.application;

import io.dough.api.common.application.enums.Role;
import io.dough.api.useCases.auth.signup.application.domain.model.SignupUser;

/** 회원가입 유스케이스에서 요구하는 영속성 작업을 정의한 출력 포트입니다. */
public interface ManageSignup {
  /**
   * 해당 이메일과 권한을 가진 사용자가 이미 존재하는지 확인합니다.
   *
   * @param email 이메일
   * @param role 권한
   * @return 존재 여부 (true: 존재함, false: 존재하지 않음)
   */
  boolean existsByEmailAndRole(String email, Role role);

  /**
   * 새로운 사용자 정보를 영구 저장소에 저장합니다.
   *
   * @param user 저장할 사용자 정보
   * @return 저장된 사용자 정보 (ID 포함)
   */
  SignupUser save(SignupUser user);
}
