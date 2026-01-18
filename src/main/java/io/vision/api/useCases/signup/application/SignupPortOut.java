package io.vision.api.useCases.signup.application;

import io.vision.api.common.application.enums.Role;
import io.vision.api.useCases.signup.application.model.SignupUser;

public interface SignupPortOut {
  boolean existsByEmailAndRole(String email, Role role);

  void save(SignupUser user);
}
