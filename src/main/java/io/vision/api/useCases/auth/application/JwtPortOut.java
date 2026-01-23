package io.vision.api.useCases.auth.application;

import io.vision.api.common.application.enums.Role;
import java.util.Optional;

public interface JwtPortOut {

  record JwtUser(String email, String displayName, Role role) {}

  Optional<JwtUser> findRefreshToken(String refreshToken);

  void saveRefreshToken(String email, String refreshToken);
}
