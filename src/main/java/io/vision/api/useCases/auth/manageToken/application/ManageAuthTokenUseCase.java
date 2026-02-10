package io.vision.api.useCases.auth.manageToken.application;

import io.vision.api.useCases.auth.manageToken.application.domain.model.CreateTokenCmd;
import io.vision.api.useCases.auth.manageToken.application.domain.model.AuthToken;
import io.vision.api.useCases.auth.manageToken.application.domain.model.RefreshTokenCmd;

/**
 * 인증 토큰(JWT)의 생성, 검증, 갱신 및 클레임 정보 추출을 담당하는 유스케이스입니다.
 */
public interface ManageAuthTokenUseCase {
  /**
   * 사용자 정보를 기반으로 새로운 인증 토큰 세트를 생성합니다.
   *
   * @param cmd 토큰 생성에 필요한 사용자 정보 (이메일, 이름, 권한)
   * @return 생성된 액세스 토큰과 리프레시 토큰
   */
  AuthToken createToken(CreateTokenCmd cmd);

  /**
   * 토큰의 유효성을 검증합니다.
   *
   * @param token 검증할 토큰 문자열
   * @return 유효한 토큰인 경우 true, 그렇지 않은 경우 false
   */
  boolean validateToken(String token);

  /**
   * 리프레시 토큰을 사용하여 새로운 토큰 세트를 발급합니다.
   *
   * @param cmd 리프레시 토큰 정보
   * @return 갱신된 액세스 토큰과 리프레시 토큰
   */
  AuthToken refreshToken(RefreshTokenCmd cmd);

  /**
   * 토큰에서 사용자의 식별자(Subject, 이메일)를 추출합니다.
   *
   * @param token 토큰 문자열
   * @return 사용자 식별자
   */
  String getSubject(String token);

  /**
   * 토큰에서 사용자의 표시 이름을 추출합니다.
   *
   * @param token 토큰 문자열
   * @return 사용자 이름
   */
  String getDisplayName(String token);

  /**
   * 토큰에서 사용자의 권한(Role) 정보를 추출합니다.
   *
   * @param token 토큰 문자열
   * @return 사용자 권한
   */
  String getRole(String token);

  /**
   * 토큰에서 사용자가 보유한 세부 특권(Privileges) 목록을 추출합니다.
   *
   * @param token 토큰 문자열
   * @return 특권 목록
   */
  java.util.List<String> getPrivileges(String token);
}
