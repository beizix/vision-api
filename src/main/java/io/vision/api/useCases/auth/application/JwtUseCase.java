package io.vision.api.useCases.auth.application;

import io.vision.api.useCases.auth.application.model.AuthCmd;
import io.vision.api.useCases.auth.application.model.AuthToken;
import io.vision.api.useCases.auth.application.model.RefreshTokenCmd;

public interface JwtUseCase {
  AuthToken createToken(AuthCmd cmd);

  boolean validateToken(String token);

  AuthToken refreshToken(RefreshTokenCmd cmd);

  String getSubject(String token);

  String getDisplayName(String token);

  java.util.List<String> getRoles(String token);
}
