package io.vision.api.useCases.auth.application;

import io.vision.api.common.application.enums.Role;
import java.util.Optional;

public interface RefreshTokenPortOut {

  record RefreshUser(String email, String displayName, Role role) {}

  Optional<RefreshUser> get(String refreshToken);

  void save(String email, String refreshToken);
}
