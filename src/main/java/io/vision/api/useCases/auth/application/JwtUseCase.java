package io.vision.api.useCases.auth.application;

import io.vision.api.useCases.auth.application.model.CreateTokenCmd;
import io.vision.api.useCases.auth.application.model.AuthToken;
import io.vision.api.useCases.auth.application.model.RefreshTokenCmd;

public interface JwtUseCase {
  AuthToken createToken(CreateTokenCmd cmd);

  boolean validateToken(String token);

  AuthToken refreshToken(RefreshTokenCmd cmd);

  String getSubject(String token);

  String getDisplayName(String token);

  String getRole(String token);

  java.util.List<String> getPrivileges(String token);
}
