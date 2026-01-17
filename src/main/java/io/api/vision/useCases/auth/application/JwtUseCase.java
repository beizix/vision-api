package io.api.vision.useCases.auth.application;

import io.api.vision.useCases.auth.application.model.AuthCmd;
import io.api.vision.useCases.auth.application.model.AuthToken;
import io.api.vision.useCases.auth.application.model.RefreshTokenCmd;

public interface JwtUseCase {
    AuthToken createToken(AuthCmd cmd);
    boolean validateToken(String token);
    AuthToken refreshToken(RefreshTokenCmd cmd);
    String getSubject(String token);
}
