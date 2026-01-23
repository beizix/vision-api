package io.vision.api.useCases.signup.adapters.persistence;

import io.vision.api.common.adapters.persistence.entity.UserEntity;
import io.vision.api.common.adapters.persistence.repository.UserRepository;
import io.vision.api.common.application.enums.Role;
import io.vision.api.useCases.signup.application.SignupPortOut;
import io.vision.api.useCases.signup.application.model.SignupUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignupPersistAdapter implements SignupPortOut {

  private final UserRepository userRepository;

  @Override
  public boolean existsByEmailAndRole(String email, Role role) {
    return userRepository.existsByEmailAndRole(email, role);
  }

  @Override
  public void save(SignupUser user) {
    UserEntity entity =
        new UserEntity(user.email(), user.password(), user.displayName(), user.role(), null);
    userRepository.save(entity);
  }
}
